package com.amplio.rdict.review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;

public class StatisticsManager {
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat( "yyyyMMdd" );

	private static final double MAX_SCORE = 4;

	private ODB odb = null;

	private CardSetManager cardSetManager;
	

	public StatisticsManager(ODB db, CardSetManager cardSetManager ) {
		this.odb = db;
		this.cardSetManager = cardSetManager;
	}

	public int calculateStudyGrade() {
		double total = 0;

		Vector<Card> cards = cardSetManager.loadCardsByPrefix( "" );

		for( int i = 0; i < cards.size(); i++ ) {
			Card c = (Card) cards.get( i );
			total += c.getScoreHistory().calcAvg() / MAX_SCORE;
		}

		return new Double( 100 * (total / cards.size()) ).intValue();
	}

	public StatRecord loadStatRecordByDate( String date ) {
		IQuery query = odb.criteriaQuery( StatRecord.class, Where.equal( "recorded", date ) );
		Objects<StatRecord> records = odb.getObjects( query );

		if( 1 == records.size() )
			return records.getFirst();
		else if( 1 < records.size() )
			throw new IllegalStateException( "Duplicate StatRecords exist for date: " + date );
		else
			return null;
	}

	public Number[] fetchCardCountData( String cutoffDate ) {
		int numDaysBetweenCutoffDateAndNow = calcNumberOfDaysBetweenDateAndNow( cutoffDate );
		Number[] cardCounts = new Number[numDaysBetweenCutoffDateAndNow];
		
		IQuery query = odb.criteriaQuery( StatRecord.class, Where.gt( "recorded", cutoffDate ) );
		query.orderByAsc( "recorded" );
		
		Objects<StatRecord> objects = odb.getObjects( query );
		Vector<StatRecord> records = new Vector<StatRecord>( objects );
		
		Calendar startDate = this.getStartDate(cutoffDate);

		int recordIndex = 0;

		for( int i = 0; i < numDaysBetweenCutoffDateAndNow; i++ ) {
			if( recordIndex < records.size() ) {
				StatRecord record = (StatRecord) records.get(recordIndex);

				if( record.recorded.equals( SIMPLE_DATE_FORMAT.format( startDate.getTime() ) ) ) {
					cardCounts[i] = new Integer( record.cardCount );
					recordIndex++;
				} else {
					cardCounts[i] = new Integer(0);
				}
			}
			else {
				cardCounts[i] = new Integer(0);
			}

			startDate.add( Calendar.HOUR_OF_DAY, 24 );
		}

		return cardCounts;
	}
	
	public Calendar getStartDate(String cutoffDate) {
		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime( SIMPLE_DATE_FORMAT.parse( cutoffDate ) );
		} catch( ParseException e ) {
			e.printStackTrace();
		}

		cal.add( Calendar.HOUR, 24);
		
		return cal;
	}

	public Number[] fetchGradeData( String cutOffDate ) {
		IQuery query = odb.criteriaQuery( StatRecord.class, Where.gt( "recorded", cutOffDate ) );
		query.orderByAsc( "recorded" );
		Objects<StatRecord> objects = odb.getObjects( query );
		Vector<StatRecord> records = new Vector<StatRecord>( objects );

		int numDaysBetweenCutOffDateAndNow = calcNumberOfDaysBetweenDateAndNow( cutOffDate );
		Number[] grades = new Number[numDaysBetweenCutOffDateAndNow];

		Calendar startDate = Calendar.getInstance();
		SimpleDateFormat sdf = SIMPLE_DATE_FORMAT;

		try {
			startDate.setTime( sdf.parse( cutOffDate ) );
		} catch( ParseException e ) {
			e.printStackTrace();
		}

		startDate.add( Calendar.HOUR_OF_DAY, 24 );

		int recordIndex = 0;
		int prevGrade = 0;

		for( int i = 0; i < numDaysBetweenCutOffDateAndNow; i++ ) {
			if( recordIndex < records.size() ) {
				StatRecord record = (StatRecord) records.get( recordIndex );

				if( record.recorded.equals( sdf.format( startDate.getTime() ) ) ) {
					grades[i] = new Integer( record.gradeInPercent );
					recordIndex++;
				} else {
					grades[i] = new Integer( prevGrade );
				}
			} else {
				grades[i] = new Integer( prevGrade );
			}

			prevGrade = grades[i].intValue();
			startDate.add( Calendar.HOUR_OF_DAY, 24 );
		}

		return grades;
	}

	public int calcNumberOfDaysBetweenDateAndNow( String date ) {
		SimpleDateFormat sdf = SIMPLE_DATE_FORMAT;
		Date d = null;

		try {
			d = sdf.parse( date );
		} catch( ParseException e ) {
			e.printStackTrace();
		}

		return new Long( (new Date().getTime() - d.getTime()) / ((long) 1000 * 60 * 60 * 24) )
		        .intValue();
	}

	public void saveOrUpdateCardStackStatistics() {
		String todaysDate = SIMPLE_DATE_FORMAT.format( new Date() );

		StatRecord record = this.loadStatRecordByDate( todaysDate );

		if( record != null ) {
			record.cardCount = this.cardSetManager.count();
			record.gradeInPercent = this.calculateStudyGrade();
		} else {
			record = new StatRecord( this.cardSetManager.count(), this.calculateStudyGrade(), todaysDate );
		}

		odb.store( record );
	}

	public void deleteAllStatRecords() {
		Objects<StatRecord> objects = odb.getObjects( StatRecord.class, false );
		for( StatRecord record : objects ) {
			odb.delete( record, true );
		}
	}
}
