package com.amplio.rdict.review;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

public class StatisticsManagerTest extends TestCase {
	
	public static final String DB_TEST_FILE = "test.db";

	private ODB db = null;
	private CardSetManager cardSetManager;
	private StatisticsManager statisticsManager;

	public void setUp() {
		db = ODBFactory.open( DB_TEST_FILE );
		cardSetManager = new CardSetManager( db );
		statisticsManager = new StatisticsManager(db, cardSetManager);
	}

	public void tearDown() {
		db.rollback();
		db.close();
	}
	
	public void testCalculateGrade() {
		Card aCard = new Card("apple" , "the answer");
		Card bCard = new Card("banana", "the answer");
		Card cCard= new Card("coconunt", "the answer");
		
		aCard.sh = ScoreHistory.createFromString("4,4,4");
		bCard.sh = ScoreHistory.createFromString("0,0,0");
		cCard.sh = ScoreHistory.createFromString("2,2,2");

		db.store(aCard);
		db.store(bCard);
		db.store(cCard);
		
		
		int grade = statisticsManager.calculateStudyGrade();
		
		assertEquals(50, grade);
	}
	
	public void testCountCards() {
		Card aCard = new Card("apple" , "the answer");
		Card bCard = new Card("banana", "the answer");
		
		Card cCard= new Card("coconunt", "the answer");
		cCard.lookedup = new DateTime().minusDays( 10 ).toDate();
		
		db.store(aCard);
		db.store(bCard);
		db.store(cCard);
		
		
		assertEquals(3, cardSetManager.count());
	}
	
	public void testLoadStatRecordByDate(){
		StatRecord statRecord = new StatRecord(1, 30, "19700101");
		
		
		db.store(statRecord);
		
		StatRecord record = statisticsManager.loadStatRecordByDate("19700101");
		
		assertEquals("19700101", record.recorded);
		
		record = statisticsManager.loadStatRecordByDate("20120101");
		
		assertEquals(null, record);
	}
	
	public void testLoadStatRecordByDatIfDuplicateExists(){
		StatRecord statRecord = new StatRecord(1, 30, "19700101");
		StatRecord statRecord2 = new StatRecord(1, 30, "19700101");
		
		
		db.store(statRecord);
		db.store(statRecord2);
		
		try{
			statisticsManager.loadStatRecordByDate("19700101");
			fail();
		}
		catch(IllegalStateException ignore){}
	}
	
	public void testRecordCardStackStaticsAddsNewRecordIfNoneExistsForToday() {
		Card aCard = new Card("apple" , "the answer");
		Card bCard = new Card("banana", "the answer");
		Card cCard= new Card("coconunt", "the answer");
		
		aCard.sh = ScoreHistory.createFromString("4,4,4");
		bCard.sh = ScoreHistory.createFromString("0,0,0");
		cCard.sh = ScoreHistory.createFromString("2,2,2");

		db.store(aCard);
		db.store(bCard);
		db.store(cCard);
		
		
		statisticsManager.saveOrUpdateCardStackStatistics();
		
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		StatRecord r = statisticsManager.loadStatRecordByDate(today);
		
		assertEquals(today, r.recorded);
		assertEquals(3, r.cardCount);
	}
	
	public void testRecordCardStackStaticsUpdatesExistingRecord() {
		Card aCard = new Card("apple" , "the answer");
		Card bCard = new Card("banana", "the answer");
		Card cCard= new Card("coconunt", "the answer");
		cCard.lookedup = new DateTime().minusDays( 10 ).toDate();
		
		aCard.sh = ScoreHistory.createFromString("4, 4, 4");
		bCard.sh = ScoreHistory.createFromString("0, 0, 0");
		cCard.sh = ScoreHistory.createFromString("2, 2, 2");

		db.store(aCard);
		db.store(bCard);
		db.store(cCard);
		
		
		statisticsManager.saveOrUpdateCardStackStatistics();
		
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		StatRecord r = statisticsManager.loadStatRecordByDate(today);
		
		assertEquals(today, r.recorded);
		assertEquals(3, r.cardCount);
		
		db.store(new Card("fish", "the definition for fish"));
		
		statisticsManager.saveOrUpdateCardStackStatistics();
		
		r = statisticsManager.loadStatRecordByDate(today);
		
		assertEquals(today, r.recorded);
		assertEquals(4, r.cardCount);
	}
	
	public void testRecordCardStackStatistics() {
		Card aCard = new Card("apple" , "the answer");
		Card bCard = new Card("banana", "the answer");
		Card cCard= new Card("coconunt", "the answer");
		
		aCard.sh = ScoreHistory.createFromString("4, 4, 4");
		bCard.sh = ScoreHistory.createFromString("0, 0, 0");
		cCard.sh = ScoreHistory.createFromString("2, 2, 2");

		db.store(aCard);
		db.store(bCard);
		db.store(cCard);
		
		
		statisticsManager.saveOrUpdateCardStackStatistics();
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		c.add(Calendar.HOUR_OF_DAY, -48);
		String twoDaysAgo = sdf.format(c.getTime());
		
		Number[] pastGrades = statisticsManager.fetchGradeData(twoDaysAgo);
		
		assertEquals(0, pastGrades[0]);
		assertEquals(50, pastGrades[1]);
		
		Number[] pastCardCounts = statisticsManager.fetchCardCountData(twoDaysAgo);

		assertEquals(0, pastCardCounts[0]);
		assertEquals(3, pastCardCounts[1]);
	}
	
	public void testFetchCardCountData() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		
		String todaysDate = sdf.format(c.getTime());
		
		c.add(Calendar.HOUR_OF_DAY, -24);
		String yesterdaysDate = sdf.format(c.getTime());
		
		c.add(Calendar.HOUR_OF_DAY, -24 * 9);
		String tenDaysBeforeDate = sdf.format(c.getTime());
		
		c.add(Calendar.HOUR_OF_DAY, -24);
		String elevenDaysBeforeDate = sdf.format(c.getTime());
		
		int cardCount = 50;
		int gradeInPercent = 70;
		
		StatRecord statRecord1 = new StatRecord(cardCount, gradeInPercent, todaysDate);
		StatRecord statRecord2 = new StatRecord(4, 50, yesterdaysDate);
		StatRecord statRecord3 = new StatRecord(1, 20, tenDaysBeforeDate);
		
		
		db.store(statRecord1);
		db.store(statRecord2);
		db.store(statRecord3);
		
		String cutOffDate = elevenDaysBeforeDate;
		
		Number[] pastCardCounts = null;
		
		pastCardCounts = statisticsManager.fetchCardCountData(cutOffDate);
		
		assertEquals(1, pastCardCounts[0]);
		assertEquals(0, pastCardCounts[1]);
		assertEquals(0, pastCardCounts[2]);
		assertEquals(0, pastCardCounts[3]);
		assertEquals(0, pastCardCounts[4]);
		assertEquals(0, pastCardCounts[5]);
		assertEquals(0, pastCardCounts[6]);
		assertEquals(0, pastCardCounts[7]);
		assertEquals(0, pastCardCounts[8]);
		assertEquals(4, pastCardCounts[9]);
		assertEquals(50, pastCardCounts[10]);
	}
	
	public void testFetchGradeData() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		
		String todaysDate = sdf.format(c.getTime());
		
		c.add(Calendar.HOUR_OF_DAY, -24);
		String yesterdaysDate = sdf.format(c.getTime());
		
		c.add(Calendar.HOUR_OF_DAY, -24 * 9);
		String tenDaysBeforeDate = sdf.format(c.getTime());
		
		c.add(Calendar.HOUR_OF_DAY, -24);
		String elevenDaysBeforeDate = sdf.format(c.getTime());
		
		int cardCount = 50;
		int gradeInPercent = 70;
		
		StatRecord statRecord1 = new StatRecord(cardCount, gradeInPercent, todaysDate);
		StatRecord statRecord2 = new StatRecord(4, 50, yesterdaysDate);
		StatRecord statRecord3 = new StatRecord(1, 20, tenDaysBeforeDate);
		
		
		db.store(statRecord1);
		db.store(statRecord2);
		db.store(statRecord3);
		
		String cutOffDate = elevenDaysBeforeDate;
		
		Number[] pastGrades = statisticsManager.fetchGradeData(cutOffDate);
		
		assertEquals(20, pastGrades[0]);
		assertEquals(20, pastGrades[1]);
		assertEquals(20, pastGrades[2]);
		assertEquals(20, pastGrades[3]);
		assertEquals(20, pastGrades[4]);
		assertEquals(20, pastGrades[5]);
		assertEquals(20, pastGrades[6]);
		assertEquals(20, pastGrades[7]);
		assertEquals(20, pastGrades[8]);
		assertEquals(50, pastGrades[9]);
		assertEquals(70, pastGrades[10]);
	}
	
	public void testFetchDataIfNoStatRecords() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, -24 * 10);
		String tenDaysBeforeDate = sdf.format(c.getTime());
		

		Number[] pastGrades = statisticsManager.fetchGradeData(tenDaysBeforeDate);
		
		assertEquals(10, pastGrades.length);
		
		for(int i = 0 ; i < pastGrades.length; i++)
			assertEquals(0, pastGrades[i]);
		
		Number[] pastCardCounts = statisticsManager.fetchCardCountData(tenDaysBeforeDate);
		
		assertEquals(10, pastCardCounts.length);
		
		for(int i = 0 ; i < pastCardCounts.length; i++)
			assertEquals(0, pastCardCounts[i]);
	}
	
	public void testCalcNumberOfDaysBetweenDateAndNow() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		
		String todaysDate = sdf.format(c.getTime());
		
		c.add(Calendar.HOUR_OF_DAY, -24);
		String yesterdaysDate = sdf.format(c.getTime());
		
		c.add(Calendar.HOUR_OF_DAY, -24 * 9);
		String tenDaysBeforeDate = sdf.format(c.getTime());
		 
		c.add(Calendar.HOUR_OF_DAY, -24 * 20);
		String thirtyDaysBeforeDate = sdf.format(c.getTime());
		
		
		assertEquals(0, statisticsManager.calcNumberOfDaysBetweenDateAndNow(todaysDate));
		assertEquals(1, statisticsManager.calcNumberOfDaysBetweenDateAndNow(yesterdaysDate));
		assertEquals(10, statisticsManager.calcNumberOfDaysBetweenDateAndNow(tenDaysBeforeDate));
		assertEquals(30, statisticsManager.calcNumberOfDaysBetweenDateAndNow(thirtyDaysBeforeDate));
	}
}
