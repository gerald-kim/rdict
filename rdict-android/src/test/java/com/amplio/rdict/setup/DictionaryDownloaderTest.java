package com.amplio.rdict.setup;

import java.io.File;

import junit.framework.TestCase;

public class DictionaryDownloaderTest extends TestCase {
	
	public void testStartDownload() {
		DownloadList downloadList = new DownloadList();
		downloadList.add("http://www.google.ca/intl/en_ca/images/logo.gif", "src/com/amplio/rdict/tests/setup/word.db");
		
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
	
	public void testDownloadIndexWithAuthentication() {
		DownloadList downloadList = new DownloadList();
		downloadList.add(DictionaryDownloader.SOURCE_URL_INDEX, "src/com/amplio/rdict/tests/setup/word.index");
		
		DownloadMonitor downloadMonitor = new DownloadMonitor(null, null);

		boolean doMd5Check = true;
		DictionaryDownloader downloader = new DictionaryDownloader(downloadList, downloadMonitor, doMd5Check);
		
		downloader.start();

		while(! downloadMonitor.isFinished())
			;

		File downloadedFile = new File(downloadList.get(0).m_writePath);

		assertTrue( downloadedFile.exists() );
		assertEquals( 242789, downloadedFile.length() );
		assertEquals( 242821, downloadMonitor.m_numBytesToDownload);
		assertEquals( 242821, downloadMonitor.m_bytesDownloaded);
	}

}
