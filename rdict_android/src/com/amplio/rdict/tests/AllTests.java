package com.amplio.vkbl8r.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.amplio.vkbl8r.tests.history.HistoryTest;
import com.amplio.vkbl8r.tests.review.CardSetManagerTest;
import com.amplio.vkbl8r.tests.review.CardTest;
import com.amplio.vkbl8r.tests.review.ReviewManagerTest;
import com.amplio.vkbl8r.tests.review.ScoreHistoryTest;
import com.amplio.vkbl8r.tests.review.SparklineTest;
import com.amplio.vkbl8r.tests.review.StatisticsManagerTest;
import com.amplio.vkbl8r.tests.search.DictionaryEntryFactoryTest;
import com.amplio.vkbl8r.tests.search.DictionaryEntryTest;
import com.amplio.vkbl8r.tests.search.DictionaryTest;
import com.amplio.vkbl8r.tests.setup.DownloadManagerTest;
import com.amplio.vkbl8r.tests.setup.SetupManagerTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.amplio.vkbl8r.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(HistoryTest.class);
		suite.addTestSuite(CardSetManagerTest.class);
		suite.addTestSuite(CardTest.class);
		suite.addTestSuite(ReviewManagerTest.class);
		suite.addTestSuite(ScoreHistoryTest.class);
		suite.addTestSuite(SparklineTest.class);
		suite.addTestSuite(StatisticsManagerTest.class);
		suite.addTestSuite(DictionaryEntryFactoryTest.class);
		suite.addTestSuite(DictionaryEntryTest.class);
		suite.addTestSuite(DictionaryTest.class);
		suite.addTestSuite(SetupManagerTest.class);
		suite.addTestSuite(DownloadManagerTest.class);
		//$JUnit-END$
		return suite;
	}

}
