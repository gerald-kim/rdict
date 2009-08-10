package com.amplio.rdict.review;

import java.util.Vector;

import org.joda.time.DateMidnight;
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

	public Card create( String question, String answer ) {
		Card card = loadCardByHeadword( question );
		if ( null == card ) {
			card = new Card( question, answer );
		} else {
			card.answer = card.answer + "\n-------\n" + answer;
			card.study( 0 );
			//reset card schedule
//			card.
//			card.getAbbreviatedAnswer( answer, maxLength )
		}
		save( card );
		return card;
	}
	
	public void save( Card card ) {
		db.store( card );
	}

	public void deleteCard( Card card ) {
		db.delete( card.getScoreHistory(), false );
		db.delete( card, false );
	}

	public Vector<Card> loadCardsScheduledForToday() {
		DateMidnight todayMidnight = new DateMidnight().plusDays( 1 );
		IQuery query = this.db.criteriaQuery( Card.class, Where.lt( "scheduled", todayMidnight.toDate() ) );
        Objects<Card> cards = this.db.getObjects( query );
        return new Vector<Card>( cards );
	}

	public Vector<Card> loadCardsLookedupToday() {
		DateMidnight today = new DateMidnight();
		
		IQuery query = this.db.criteriaQuery( Card.class, Where.gt( "lookedup", today.toDate() ) );
        Objects<Card> cards = this.db.getObjects( query );
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
