package com.amplio.vcbl8r.setup;

import java.io.File;

import com.amplio.vcbl8r.setup.DownloadMonitor;

import junit.framework.TestCase;

public class DownloadMonitorTest extends TestCase {
	private static final String TARGET_PATH = "tmp.db";
	
	public void tearDown() {
		File downloadedFile = new File( TARGET_PATH );
		downloadedFile.delete();

		assertTrue( !downloadedFile.exists() );
	}
	
	public void testIsDownloading() {
		DownloadMonitor downloadMgr = new DownloadMonitor(null, null);
		
		assertTrue(! downloadMgr.isDownloading());
		
		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILE_SIZES);
		
		assertTrue(downloadMgr.isDownloading());
		
		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILES);
		
		assertTrue(downloadMgr.isDownloading());
		
		downloadMgr.setState(DownloadMonitor.STATE_FINISHED_DOWNLOAD_ONLY);
		
		assertTrue(! downloadMgr.isDownloading());
	}
	
	public void testIsVerifying() {
		DownloadMonitor downloadMgr = new DownloadMonitor(null, null);

		assertTrue(! downloadMgr.isVerifying());
		
		downloadMgr.setState(DownloadMonitor.STATE_VERIFYING_FILES);
		
		assertTrue(downloadMgr.isVerifying());
		
		downloadMgr.setState(DownloadMonitor.STATE_FINISHED_CHECKING_FAILED);
		
		assertTrue(! downloadMgr.isVerifying());
		
		downloadMgr.setState(DownloadMonitor.STATE_FINISHED_CHECKING_SUCCESS);
		
		assertTrue(! downloadMgr.isVerifying());
	}
	
	public void testIsFinished() {
		DownloadMonitor downloadMgr = new DownloadMonitor(null, null);

		assertTrue(! downloadMgr.isFinished());
		
		downloadMgr.setState(DownloadMonitor.STATE_VERIFYING_FILES);
		
		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILE_SIZES);
		
		assertTrue(! downloadMgr.isFinished());
		
		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILES);
		
		assertTrue(! downloadMgr.isFinished());
		
		downloadMgr.setState(DownloadMonitor.STATE_FINISHED_CHECKING_FAILED);
		
		assertTrue(downloadMgr.isFinished());
		
		downloadMgr.setState(DownloadMonitor.STATE_FINISHED_CHECKING_SUCCESS);
		
		assertTrue(downloadMgr.isFinished());
		
		downloadMgr.setState(DownloadMonitor.STATE_FINISHED_DOWNLOAD_ONLY);
		
		assertTrue(downloadMgr.isFinished());
	}
	
	public void testGetProgress() {
		DownloadMonitor downloadMgr = new DownloadMonitor(null, null);

		downloadMgr.numBytesToDownload = 500;
		downloadMgr.bytesDownloaded = 0;

		assertEquals( 0, downloadMgr.getProgress() );

		downloadMgr.bytesDownloaded = 50;

		assertEquals( 10, downloadMgr.getProgress() );

		downloadMgr.bytesDownloaded = 500;

		assertEquals( 100, downloadMgr.getProgress() );
	}
	
//	public void testStateForDownloadWithNoChecking() {
//		DownloadMonitor downloadMgr = new DownloadMonitor(null, null);
//
//		assertEquals(DownloadMonitor.STATE_NOT_STARTED, downloadMgr.getState());
//		
//		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILE_SIZES);
//		
//		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILES);
//		
//		downloadMgr.setState(DownloadMonitor.STATE_FINISHED_DOWNLOAD);
//	}
//	
//	public void testStateForDownloadWithCheckingSuccessful() {
//		DownloadMonitor downloadMgr = new DownloadMonitor(null, null);
//
//		assertEquals(DownloadMonitor.STATE_NOT_STARTED, downloadMgr.getState());
//		
//		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILE_SIZES);
//		
//		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILES);
//		
//		downloadMgr.setState(DownloadMonitor.STATE_VERIFYING_FILES);
//		
//		downloadMgr.setState(DownloadMonitor.STATE_FINISHED_CHECKING_SUCCESS);
//	}
//	
//	public void testStateForDownloadWithCheckingSuccessful() {
//		DownloadMonitor downloadMgr = new DownloadMonitor(null, null);
//
//		assertEquals(DownloadMonitor.STATE_NOT_STARTED, downloadMgr.getState());
//		
//		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILE_SIZES);
//		
//		downloadMgr.setState(DownloadMonitor.STATE_DOWNLOADING_FILES);
//		
//		downloadMgr.setState(DownloadMonitor.STATE_VERIFYING_FILES);
//		
//		downloadMgr.setState(DownloadMonitor.STATE_FINISHED_CHECKING_FAILED);
//	}
}
