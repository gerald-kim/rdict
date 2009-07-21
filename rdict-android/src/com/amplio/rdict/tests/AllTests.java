package com.amplio.rdict.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.amplio.rdict.tests.history.HistoryTest;
import com.amplio.rdict.tests.review.CardSetManagerTest;
import com.amplio.rdict.tests.review.CardTest;
import com.amplio.rdict.tests.review.EasinessHistoryTest;
import com.amplio.rdict.tests.review.ReviewManagerTest;
import com.amplio.rdict.tests.review.StatisticsManagerTest;
import com.amplio.rdict.tests.search.DictionaryEntryFactoryTest;
import com.amplio.rdict.tests.search.DictionaryEntryTest;
import com.amplio.rdict.tests.search.DictionaryTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.amplio.rdict.tests.history");
		//$JUnit-BEGIN$
		suite.addTestSuite(HistoryTest.class);
		suite.addTestSuite(CardSetManagerTest.class);
		suite.addTestSuite(CardTest.class);
		suite.addTestSuite(EasinessHistoryTest.class);
		suite.addTestSuite(ReviewManagerTest.class);
		suite.addTestSuite(StatisticsManagerTest.class);
		suite.addTestSuite(DictionaryEntryFactoryTest.class);
		suite.addTestSuite(DictionaryEntryTest.class);
		suite.addTestSuite(DictionaryTest.class);
		//$JUnit-END$
		return suite;
	}

}
