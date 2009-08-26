package com.amplio.rdict.review;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.joda.time.DateMidnight;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.core.query.nq.SimpleNativeQuery;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

import com.amplio.rdict.search.Dictionary;

public class CardSetManager {
	private ODB db = null;
	List<Card> allCards;

	public CardSetManager( ODB odb ) {
		db = odb;
	}

	public Card create( String question, String answer ) {
		Card card = loadCardByHeadword( question );
		if ( null == card ) {
			card = new Card( question, answer );
		} else {
			card.answer = card.answer + "\n-------\n" + answer;
			card.scheduleByGrade( 0 );
			//reset card schedule
//			card.
//			card.getAbbreviatedAnswer( answer, maxLength )
		}
		save( card );
		return card;
	}
	
	public void save( Card card ) {
		db.store(card);
	}

	public void deleteCard( Card card ) {
		db.delete( card.getScoreHistory(), false );
		db.delete( card, false );
	}

	public Vector<Card> loadCardsScheduledForToday() {
		DateMidnight todayMidnight = new DateMidnight().plusDays( 1 );
		IQuery query = this.db.criteriaQuery( Card.class, Where.lt( "scheduled", todayMidnight.toDate() ) );
        Objects<Card> cards = this.db.getObjects(query);
        return new Vector<Card>( cards );
	}
	
	public int countCardsScheduledForToday() {
		DateMidnight todayMidnight = new DateMidnight().plusDays( 1 );
		CriteriaQuery query = this.db.criteriaQuery( Card.class, Where.lt( "scheduled", todayMidnight.toDate() ) );
        return this.db.count(query).intValue();
	}

	public int count() {
		return db.getObjects( Card.class ).size();
	}

	@SuppressWarnings( "unchecked" )
    public List<Card> loadAllCards() {
		allCards = new ArrayList( db.getObjects( Card.class ) );
		
		Collections.sort( allCards, new Comparator<Card>() {
			public int compare( Card object1, Card object2 ) {
	            return Dictionary.COLLATOR.compare( object1.question, object2.question );
            }
		});

		return allCards;
	} 
	
	class MockCard extends Card {
		public MockCard( String question ) {
			super( question, null );
		}
		public MockCard( String question, String answer ) {
	        super( question, answer );
        }
	}

	public int findCardIndexByWordPrefix( String word ) {
		int wordIndex = 0;
		int idx = 0;
		while( wordIndex < word.length()) {
			wordIndex++;
			idx = Collections.binarySearch(allCards,
											new MockCard(word.substring( 0, wordIndex ) ), 
											new Comparator<Card>() {
												public int compare( Card object1, Card object2 ) {
													return Dictionary.COLLATOR.compare(object1.question, object2.question );
												}
											});
			if(idx < 0) {
				if ( -idx - 1 >= allCards.size() - 1) return allCards.size() - 1;
				else if( ! reachedCardStartingWithPrefix(word, wordIndex, idx )) return -idx - 1;
			}
		}

		if( idx < 0 )
			idx = -idx - 1;
		return idx;
	}
	
	private boolean reachedCardStartingWithPrefix(String prefix, int wordIndex, int idx) {
	    if((-idx - 1)  < allCards.size()) {
			String temp_prefix = prefix.substring(0, wordIndex ).toLowerCase();
			String question = allCards.get(-idx).question.toLowerCase();
			return question.startsWith(temp_prefix);
	    }
	    else {
	    	return false;
	    }
    }


	@SuppressWarnings( "serial" )
    public Vector<Card> loadCardsLookedupToday() {
		final DateMidnight today = new DateMidnight();
		
        IQuery query = new SimpleNativeQuery() {
            @SuppressWarnings( "unused" )
            public boolean match(Card card) {
                return card.lookedup.after( today.toDate() ) && ( null == card.studied || card.studied.before( card.lookedup ) );  
            }
        };

//		IQuery query = this.db.criteriaQuery( Card.class, Where.gt( "lookedup", today.toDate() ) );
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
