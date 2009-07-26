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

	private static final double MAX_SCORE = 4;

	ODB db = null;

	public CardSetManager cardsMgr = null;

	public StatisticsManager( ODB db ) {
		this.db = db;
		this.cardsMgr = new CardSetManager( db );
	}

	public int calculateStudyGrade() {
		double total = 0;

		Vector<Card> cards = this.cardsMgr.loadCardsByPrefix( "" );

		for( int i = 0; i < cards.size(); i++ ) {
			Card c = (Card) cards.get( i );
			total += c.getScoreHistory().calcAvg() / MAX_SCORE;
		}

		return new Double( 100 * (total / cards.size()) ).intValue();
	}

	public int countCards() {
		return this.cardsMgr.loadCardsLookedupToday().size();
	}

	public StatRecord loadStatRecordByDate( String date ) {
		IQuery query = db.criteriaQuery( StatRecord.class, Where.equal( "record_date", date ) );
		Objects<StatRecord> records = db.getObjects( query );

		if( 1 == records.size() )
			return records.getFirst();
		else if( 1 < records.size() )
			throw new IllegalStateException( "Duplicate StatRecords exist for date: " + date );
		else
			return null;
	}

	public Number[] fetchCardCountData( String cutOffDate ) {
		IQuery query = db.criteriaQuery( StatRecord.class, Where.gt( "record_date", cutOffDate ) );
		query.orderByAsc( "record_date" );
		Objects<StatRecord> objects = db.getObjects( query );
		Vector<StatRecord> records = new Vector<StatRecord>( objects );
		int numDaysBetweenCutOffDateAndNow = calcNumberOfDaysBetweenDateAndNow( cutOffDate );
		Number[] cardCounts = new Number[numDaysBetweenCutOffDateAndNow];

		Calendar startDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );

		try {
			startDate.setTime( sdf.parse( cutOffDate ) );
		} catch( ParseException e ) {
			e.printStackTrace();
		}

		startDate.add( Calendar.HOUR_OF_DAY, 24 );

		int recordIndex = 0;

		for( int i = 0; i < numDaysBetweenCutOffDateAndNow; i++ ) {
			if( recordIndex < records.size() ) {
				StatRecord record = (StatRecord) records.get( recordIndex );

				if( record.record_date.equals( sdf.format( startDate.getTime() ) ) ) {
					cardCounts[i] = new Integer( record.cardCount );
					recordIndex++;
				} else {
					cardCounts[i] = 0;
				}
			} else {
				cardCounts[i] = 0;
			}

			startDate.add( Calendar.HOUR_OF_DAY, 24 );
		}

		return cardCounts;
	}

	public Number[] fetchGradeData( String cutOffDate ) {
		IQuery query = db.criteriaQuery( StatRecord.class, Where.gt( "record_date", cutOffDate ) );
		query.orderByAsc( "record_date" );
		Objects<StatRecord> objects = db.getObjects( query );
		Vector<StatRecord> records = new Vector<StatRecord>( objects );

		int numDaysBetweenCutOffDateAndNow = calcNumberOfDaysBetweenDateAndNow( cutOffDate );
		Number[] grades = new Number[numDaysBetweenCutOffDateAndNow];

		Calendar startDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );

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

				if( record.record_date.equals( sdf.format( startDate.getTime() ) ) ) {
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
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
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
		String todaysDate = new SimpleDateFormat( "yyyyMMdd" ).format( new Date() );

		StatRecord record = this.loadStatRecordByDate( todaysDate );

		if( record != null ) {
			record.cardCount = this.countCards();
			record.gradeInPercent = this.calculateStudyGrade();
		} else {
			record = new StatRecord( this.countCards(), this.calculateStudyGrade(), todaysDate );
		}

		this.db.store( record );
		this.db.commit();
	}

	public void deleteAllStatRecords() {
		Objects<StatRecord> objects = db.getObjects( StatRecord.class, false );
		for( StatRecord record : objects ) {
			db.delete( record, true );
		}
	}
}
