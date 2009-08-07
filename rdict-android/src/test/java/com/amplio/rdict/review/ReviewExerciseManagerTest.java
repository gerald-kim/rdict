package com.amplio.rdict.review;

import java.util.Vector;

import junit.framework.TestCase;

public class ReviewExerciseManagerTest extends TestCase {
	
	public void testStateTravelling() {
		Vector<Card> cards = new Vector<Card>();
		cards.add(new Card("word1", "def1"));
		cards.add(new Card("word2", "def2"));
		cards.add(new Card("word3", "def3"));
		
		ReviewExerciseManager mgr = new ReviewExerciseManager(cards);
		
		assertEquals(ReviewExerciseManager.STATE_EXERCISE_STARTED, mgr.getState());
		
		mgr.userPressedViewAnswerButton();
		
		assertEquals(ReviewExerciseManager.STATE_USER_PRESSED_VIEW_ANSWER, mgr.getState());
		
		mgr.userPressedAnEasinessButton(ReviewExerciseManager.GRADE_EASY);
		
		assertEquals(ReviewExerciseManager.STATE_USER_PRESSED_EASINESS_BUTTON, mgr.getState());
	}
	
	public void testIsFinished() {
		Vector<Card> cards = new Vector<Card>();
		cards.add(new Card("word1", "def1"));
		cards.add(new Card("word2", "def2"));
		
		ReviewExerciseManager mgr = new ReviewExerciseManager(cards);
		
		assertTrue(ReviewExerciseManager.STATE_EXERCISE_FINISHED != mgr.getState());
		
		mgr.userPressedViewAnswerButton();
		mgr.userPressedAnEasinessButton(ReviewExerciseManager.GRADE_EASY);
		
		mgr.next();
		
		assertTrue(ReviewExerciseManager.STATE_EXERCISE_FINISHED != mgr.getState());
		
		mgr.userPressedViewAnswerButton();
		mgr.userPressedAnEasinessButton(ReviewExerciseManager.GRADE_EASY);
		
		mgr.next();
		
		assertTrue(ReviewExerciseManager.STATE_EXERCISE_FINISHED == mgr.getState());
	}
	
	public void testTravellingStateIncreasesCardIndex() {
		Vector<Card> cards = new Vector<Card>();
		cards.add(new Card("word1", "def1"));
		cards.add(new Card("word2", "def2"));
		cards.add(new Card("word3", "def3"));
		
		ReviewExerciseManager mgr = new ReviewExerciseManager(cards);
		
		assertEquals(0, mgr.getCardIndex());
		
		mgr.userPressedViewAnswerButton();
		mgr.userPressedAnEasinessButton(ReviewExerciseManager.GRADE_EASY);
		
		mgr.next();
		
		assertEquals(1, mgr.getCardIndex());
	}
}
