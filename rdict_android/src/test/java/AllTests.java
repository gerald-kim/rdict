

import junit.framework.Test;
import junit.framework.TestSuite;

import com.amplio.vkbl8r.NeoDatisTest;
import com.amplio.vkbl8r.StartupManagerTest;
import com.amplio.vkbl8r.history.HistoryTest;
import com.amplio.vkbl8r.review.CardSetManagerTest;
import com.amplio.vkbl8r.review.CardTest;
import com.amplio.vkbl8r.review.ReviewExerciseManagerTest;
import com.amplio.vkbl8r.review.ReviewManagerTest;
import com.amplio.vkbl8r.review.ScoreHistoryTest;
import com.amplio.vkbl8r.review.StatisticsManagerTest;
import com.amplio.vkbl8r.search.DictionaryEntryFactoryTest;
import com.amplio.vkbl8r.search.DictionaryEntryTest;
import com.amplio.vkbl8r.search.DictionaryTest;
import com.amplio.vkbl8r.setup.DictionaryDownloaderTest;
import com.amplio.vkbl8r.setup.DownloadFileTest;
import com.amplio.vkbl8r.setup.DownloadListTest;
import com.amplio.vkbl8r.setup.DownloadMonitorTest;
import com.amplio.vkbl8r.setup.DownloadUtilsTest;
import com.amplio.vkbl8r.setup.SetupManagerTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite( "Test for com.amplio.vkbl8r" );
		//$JUnit-BEGIN$
		suite.addTestSuite( NeoDatisTest.class );
		suite.addTestSuite( SetupManagerTest.class );
		suite.addTestSuite( DownloadFileTest.class );
		suite.addTestSuite( DownloadMonitorTest.class );
		suite.addTestSuite( DownloadListTest.class );
		suite.addTestSuite( DownloadUtilsTest.class );
		suite.addTestSuite( DictionaryDownloaderTest.class );
		suite.addTestSuite( DictionaryEntryFactoryTest.class );
		suite.addTestSuite( DictionaryEntryTest.class );
		suite.addTestSuite( DictionaryTest.class );
		suite.addTestSuite( StatisticsManagerTest.class );
		suite.addTestSuite( ScoreHistoryTest.class );
		suite.addTestSuite( ReviewManagerTest.class );
		suite.addTestSuite( CardSetManagerTest.class );
		suite.addTestSuite( CardTest.class );
		suite.addTestSuite( ReviewExerciseManagerTest.class );
		suite.addTestSuite( HistoryTest.class );
		suite.addTestSuite( NeoDatisTest.class );
		suite.addTestSuite( StartupManagerTest.class );
		//$JUnit-END$
		return suite;
	}

}
