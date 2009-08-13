package com.amplio.rdict.setup;

import java.io.File;

import junit.framework.TestCase;

public class DictionaryDownloaderTest extends TestCase {
	
	public void testStartDownload() {
		DownloadList downloadList = new DownloadList();
		downloadList.add("http://www.google.ca/intl/en_ca/images/logo.gif", "src/test/java/com/amplio/rdict/setup/word.db");
		
		DownloadMonitor downloadMonitor = new DownloadMonitor(null, null);
		
		boolean doMd5Check = false;
		DictionaryDownloader downloader = new DictionaryDownloader(downloadList, downloadMonitor, doMd5Check);
		
		downloader.start();

		while(! downloadMonitor.isFinished())
			;

		File downloadedFile = new File(downloadList.get(0).m_writePath);

		assertTrue( downloadedFile.exists() );
		assertEquals( 8914, downloadedFile.length() );
		assertEquals( 8914, downloadMonitor.m_numBytesToDownload);
		assertEquals( 8914, downloadMonitor.m_bytesDownloaded );
	}
	
	public void testDownloadFileWithBadMd5DeletesDownloadedFiles() {
		DownloadList downloadList = new DownloadList();
		//the md5 file is bad on purpose for testing.
		downloadList.add("http://s3.amazonaws.com/rdict/test_for_bad_md5.gif", "src/test/java/com/amplio/rdict/setup/test_for_bad_md5.gif");
		
		DownloadMonitor downloadMonitor = new DownloadMonitor(null, null);

		boolean doMd5Check = true;
		DictionaryDownloader downloader = new DictionaryDownloader(downloadList, downloadMonitor, doMd5Check);
		
		downloader.start();

		while(! downloadMonitor.isFinished())
			;

		assertTrue(! new File(downloadList.get(0).m_writePath).exists());
		assertTrue(! new File(downloadList.get(0).m_md5FileWritePath).exists());
	}

}
