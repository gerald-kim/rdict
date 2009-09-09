

import junit.framework.Test;
import junit.framework.TestSuite;

import com.amplio.rdict.NeoDatisTest;
import com.amplio.rdict.StartupManagerTest;
import com.amplio.rdict.history.HistoryTest;
import com.amplio.rdict.review.CardSetManagerTest;
import com.amplio.rdict.review.CardTest;
import com.amplio.rdict.review.ReviewExerciseManagerTest;
import com.amplio.rdict.review.ReviewManagerTest;
import com.amplio.rdict.review.ScoreHistoryTest;
import com.amplio.rdict.review.StatisticsManagerTest;
import com.amplio.rdict.search.DictionaryEntryFactoryTest;
import com.amplio.rdict.search.DictionaryEntryTest;
import com.amplio.rdict.search.DictionaryTest;
import com.amplio.rdict.setup.DictionaryDownloaderTest;
import com.amplio.rdict.setup.DownloadFileTest;
import com.amplio.rdict.setup.DownloadListTest;
import com.amplio.rdict.setup.DownloadMonitorTest;
import com.amplio.rdict.setup.DownloadUtilsTest;
import com.amplio.rdict.setup.SetupManagerTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite( "Test for com.amplio.rdict" );
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
