package com.amplio.rdict.review;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.core.query.nq.SimpleNativeQuery;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

public class CardSetManager {
	private ODB db = null;

	public CardSetManager( ODB odb ) {
		db = odb;
	}

	public void save( Card card ) {
		db.store( card );
	}

	public void deleteCard( Card card ) {
		db.delete( card.getScoreHistory(), false );
		db.delete( card, false );
	}

	public Vector<Card> loadCardsByScheduledDate( String scheduledDate ) {
		IQuery query = db.criteriaQuery( Card.class, Where.le( "date_scheduled", scheduledDate ) );
		Objects<Card> cards = db.getObjects( query );
		return new Vector<Card>( cards );

	}

	public Vector<Card> loadCardsScheduledForToday() {
		String todaysDate = new SimpleDateFormat( "yyyyMMdd" ).format( new Date() );
		return this.loadCardsByScheduledDate( todaysDate );
	}

	public Vector<Card> loadCardsLookedupToday() {
		String todaysDate = new SimpleDateFormat( "yyyyMMdd" ).format( new Date() );

		IQuery query = db.criteriaQuery( Card.class, Where.equal( "date_lookedup", todaysDate ) );
		Objects<Card> cards = db.getObjects( query );
		return new Vector<Card>( cards );
	}

	public Vector<Card> loadTopNHardestCards( int n ) {
		IQuery query = db.criteriaQuery( Card.class ).orderByAsc( "easiness" );
		Objects<Card> cards = db.getObjects( query, true, 0, n );
		return new Vector<Card>( cards );
	}

	@SuppressWarnings( "serial" )
    public Vector<Card> loadCardsByPrefix( final String prefix ) {
		IQuery query = new SimpleNativeQuery() {
			@SuppressWarnings( "unused" )
            public boolean match( Card card ) {
				return card.question.toLowerCase().startsWith( prefix.toLowerCase() );
			}
		};
		query.orderByAsc( "question" );
		Objects<Card> objects = db.getObjects( query, true, 0, 20 );
		return new Vector<Card>( objects );
	}

	public Card loadCardByHeadword( String headword ) {
		IQuery query = db.criteriaQuery( Card.class, Where.equal( "question", headword ) );
		int count = db.count( (CriteriaQuery) query ).intValue();
		if( 1 < count ) {
			throw new IllegalStateException( "Duplicates cards exist in the database." );
		} else if( 1 == count ) {
			return (Card) db.getObjects( query ).getFirst();
		} else {
			return null;
		}
	}
}
