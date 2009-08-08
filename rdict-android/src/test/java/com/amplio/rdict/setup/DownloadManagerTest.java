package com.amplio.rdict.setup;

import java.io.File;

import junit.framework.TestCase;

public class DownloadManagerTest extends TestCase {
	private static final String TARGET_PATH = "tmp.db";
	private static final String FILE_URL = "http://www.google.ca/intl/en_ca/images/logo.gif";

	public void tearDown() {
		File downloadedFile = new File( TARGET_PATH );
		downloadedFile.delete();

		assertTrue( !downloadedFile.exists() );
	}

	public void testGetRemoteFileSize() {
		int expectedSize = 8914;

		DownloadManager downloadMgr = new DownloadManager();

		int dbSize = downloadMgr.getRemoteFilesize( FILE_URL );

		assertEquals( expectedSize, dbSize );
	}

	public void testDownloadFile() {

		DownloadManager downloadMgr = new DownloadManager();

		boolean downloadResult = downloadMgr.downloadFile( FILE_URL, TARGET_PATH );

		File downloadedFile = new File( TARGET_PATH );

		assertTrue( downloadResult );
		assertTrue( downloadedFile.exists() );
		assertEquals( 8914, downloadedFile.length() );
		assertEquals( 8914, downloadMgr.tot_bytes_downloaded );
	}

	public void testGetProgress() {
		DownloadManager downloadMgr = new DownloadManager();

		downloadMgr.download_file_length = 500;
		downloadMgr.tot_bytes_downloaded = 0;

		assertEquals( 0, downloadMgr.getProgress() );

		downloadMgr.tot_bytes_downloaded = 50;

		assertEquals( 10, downloadMgr.getProgress() );

		downloadMgr.tot_bytes_downloaded = 500;

		assertEquals( 100, downloadMgr.getProgress() );
	}

	public void aTestStartDownload() {
		String[] sourceURL = new String[]{"http://www.google.ca/intl/en_ca/images/logo.gif"};
		String[] targetPath = new String[] {"src/com/amplio/rdict/tests/setup/word.db"};

		DownloadManager downloadMgr = new DownloadManager();
		downloadMgr.startDownload( sourceURL, targetPath, null, null );

		while( downloadMgr.isDownloading())
			;

		File downloadedFile = new File( targetPath[0] );

		assertTrue( downloadedFile.exists() );
		assertEquals( 8914, downloadedFile.length() );
		assertEquals( 8914, downloadMgr.tot_bytes_downloaded );
	}

}
