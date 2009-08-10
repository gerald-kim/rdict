package com.amplio.rdict.review;

import java.util.Date;

import junit.framework.TestCase;

import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;

public class CardTest extends TestCase {
	public static void assertDateEquals( Date expected, Date actual,
	        DateTimeFieldType fieldType ) {
		DateTimeComparator comparator = DateTimeComparator.getInstance( fieldType );
		assertEquals( 0, comparator.compare( expected, actual ) );

	}

	public void testConstructor() {
		Card c = new Card( "How big is it?", "Very big." );

		assertEquals( "How big is it?", c.question );
		assertEquals( "Very big.", c.answer );
		assertEquals( 2.5, c.easiness );
		assertEquals( 0, c.repsSinceLapse );
		assertEquals( 1, c.interval );

		assertDateEquals( new Date(), c.searched, DateTimeFieldType.secondOfMinute() );

		assertEquals( "0,0,0", c.getScoreHistory().toString() );
	}

	public void testRounding() {

		double twoPointTwo = 2.2;
		double twoPointSeven = 2.7;

		assertEquals( 2, (int) twoPointTwo );
		assertEquals( 2, (int) twoPointSeven );

		assertEquals( 3, (int) Math.ceil( twoPointSeven ) );
	}


	public void testDefaultEasiness() {
		Card c = new Card( "How big?", "Big." );

		assertEquals( 2.5, c.easiness );
	}

	public void testGetAbbreviatedAnswerForShortAnswer() {
		int maxLength = 40;
		assertEquals( "A short answer.", Card.getAbbreviatedAnswer( "A short answer.", maxLength ) );
	}

	public void testGetAbbreviatedAnswerForLongAnswer() {
		String longAnswer = "A really really really really really really really really "
		        + "really really really really really really really really "
		        + "really really really really really really really really "
		        + "really really really really really really really really "
		        + "really really really really really really really really "
		        + "really long answer.";

		int maxLength = 40;
		assertTrue( maxLength + "...".length() >= Card.getAbbreviatedAnswer( longAnswer, maxLength )
		        .length() );
	}

	public void testGetAbbreviatedAnswerDoesntSplitLastWord() {
		String answer = "A really big nice cow.";

		int maxLength = 15;

		assertEquals( "A really big ni", answer.substring( 0, 15 ) );

		assertEquals( "A really big...", Card.getAbbreviatedAnswer( answer, maxLength ) );

		assertEquals( "A really...", Card.getAbbreviatedAnswer( answer, 8 ) );
		assertEquals( "A really...", Card.getAbbreviatedAnswer( answer, 9 ) );
	}

}
