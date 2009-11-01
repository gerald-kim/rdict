

import junit.framework.Test;
import junit.framework.TestSuite;

import com.amplio.vcbl8r.NeoDatisTest;
import com.amplio.vcbl8r.StartupManagerTest;
import com.amplio.vcbl8r.history.HistoryTest;
import com.amplio.vcbl8r.review.CardSetManagerTest;
import com.amplio.vcbl8r.review.CardTest;
import com.amplio.vcbl8r.review.ReviewExerciseManagerTest;
import com.amplio.vcbl8r.review.ReviewManagerTest;
import com.amplio.vcbl8r.review.ScoreHistoryTest;
import com.amplio.vcbl8r.review.StatisticsManagerTest;
import com.amplio.vcbl8r.search.DictionaryEntryFactoryTest;
import com.amplio.vcbl8r.search.DictionaryEntryTest;
import com.amplio.vcbl8r.search.DictionaryTest;
import com.amplio.vcbl8r.setup.DictionaryDownloaderTest;
import com.amplio.vcbl8r.setup.DownloadFileTest;
import com.amplio.vcbl8r.setup.DownloadListTest;
import com.amplio.vcbl8r.setup.DownloadMonitorTest;
import com.amplio.vcbl8r.setup.DownloadUtilsTest;
import com.amplio.vcbl8r.setup.SetupManagerTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite( "Test for com.amplio.vcbl8r" );
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
