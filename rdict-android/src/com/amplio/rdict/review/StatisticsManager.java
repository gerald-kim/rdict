package com.amplio.rdict.review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class StatisticsManager {

	private static final double MAX_SCORE = 4;
	
	ObjectContainer db = null;
	
	public CardSetManager cardsMgr = null;
	
	public StatisticsManager(ObjectContainer db) {
		this.db = db;
		this.cardsMgr = new CardSetManager(db);
	}

	public int calculateStudyGrade() {
		double total = 0;
		
		ObjectSet cards = this.cardsMgr.loadCardsByPrefix("");
		
		for(int i = 0; i < cards.size(); i++){
			Card c = (Card) cards.get(i);
			total += c.getEasinessHistory().calcAvg() / MAX_SCORE;
		}
		
		return new Double(100 * (total / cards.size())).intValue();
	}

	public int countCards() {
		return this.cardsMgr.loadCardsByPrefix("").size();
	}
	
	public StatRecord loadStatRecordByDate(String date) {
//		Query query = this.db.query();
//		query.constrain(StatRecord.class);
//		ObjectSet records = query.execute();
//		
//		for(int i = 0; i < records.size(); i++)
//			db.delete(records.get(i));
		
		Query query = this.db.query();
		query.constrain(StatRecord.class);
		query.descend("record_date").constrain(date);
		ObjectSet records = query.execute();
		
		if(1 == records.size())
			return (StatRecord) records.get(0);
		else if (1 < records.size())
			throw new IllegalStateException("Duplicate StatRecords exist for date: " + date);
		else
			return null;	
	}
	
	public void recordCardStackStatistics() {
		String todaysDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		StatRecord record = this.loadStatRecordByDate(todaysDate);
		
		if (record != null){
			record.cardCount = this.countCards();
			record.gradeInPercent = this.calculateStudyGrade();
		}
		else {
			record = new StatRecord(this.countCards(), this.calculateStudyGrade(), todaysDate);
		}
		
		this.db.store(record);
	}

	public Number[] fetchCardCountData(String cutOffDate) {
		Query query = this.db.query();
		query.constrain(StatRecord.class);
		query.descend("record_date").constrain(cutOffDate).greater();
		query.descend("record_date").orderAscending();
		ObjectSet records = query.execute();
		
		int numDaysBetweenCutOffDateAndNow = calcNumberOfDaysBetweenDateAndNow(cutOffDate);
		Number[] cardCounts = new Number[numDaysBetweenCutOffDateAndNow];
		
		Calendar startDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		try {
			startDate.setTime(sdf.parse(cutOffDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		startDate.add(Calendar.HOUR_OF_DAY, 24);
		
		int recordIndex = 0;
		int prevCount = 0;
		
		for(int i = 0; i < numDaysBetweenCutOffDateAndNow; i++){
			if(recordIndex < records.size()){
				StatRecord record = (StatRecord) records.get(recordIndex);
				
				if(record.record_date.equals(sdf.format(startDate.getTime()))){
					cardCounts[i] = new Integer(record.cardCount);
					recordIndex++;
				}
				else {
					cardCounts[i] = new Integer(prevCount);
				}
			}
			else {
				cardCounts[i] = new Integer(prevCount);
			}
			
			prevCount = cardCounts[i].intValue();
			startDate.add(Calendar.HOUR_OF_DAY, 24);
		}
		
		return cardCounts;
	}

	public Number[] fetchGradeData(String cutOffDate) {
		Query query = this.db.query();
		query.constrain(StatRecord.class);
		query.descend("record_date").constrain(cutOffDate).greater();
		query.descend("record_date").orderAscending();
		ObjectSet records = query.execute();
		
		int numDaysBetweenCutOffDateAndNow = calcNumberOfDaysBetweenDateAndNow(cutOffDate);
		Number[] grades = new Number[numDaysBetweenCutOffDateAndNow];
		
		Calendar startDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		try {
			startDate.setTime(sdf.parse(cutOffDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		startDate.add(Calendar.HOUR_OF_DAY, 24);
		
		int recordIndex = 0;
		int prevGrade = 0;
		
		for(int i = 0; i < numDaysBetweenCutOffDateAndNow; i++){
			if(recordIndex < records.size()){
				StatRecord record = (StatRecord) records.get(recordIndex);
				
				if(record.record_date.equals(sdf.format(startDate.getTime()))){
					grades[i] = new Integer(record.gradeInPercent);
					recordIndex++;
				}
				else {
					grades[i] = new Integer(prevGrade);
				}
			}
			else {
				grades[i] = new Integer(prevGrade);
			}
			
			prevGrade = grades[i].intValue();
			startDate.add(Calendar.HOUR_OF_DAY, 24);
		}
		
		return grades;
	}
	
	public int calcNumberOfDaysBetweenDateAndNow(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = null;
		
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return new Long((new Date().getTime() - d.getTime()) / ((long) 1000 * 60 * 60 * 24)).intValue();
	}
}
