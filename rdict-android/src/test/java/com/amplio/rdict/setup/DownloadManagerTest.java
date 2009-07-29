package com.amplio.rdict.setup;

import java.io.File;

import junit.framework.TestCase;

public class DownloadManagerTest extends TestCase{
	
	public void setUp () {
		String targetPath = "src/com/amplio/rdict/tests/setup/word.db";
		File downloadedFile = new File(targetPath);
		downloadedFile.delete();
		
		assertTrue(! downloadedFile.exists());
	}
	
	public void testGetRemoteFileSize() {
		String fileURL = "http://www.google.ca/intl/en_ca/images/logo.gif";
		int expectedSize = 8914;
		
		DownloadManager downloadMgr = new DownloadManager();
		
		int dbSize = downloadMgr.getRemoteFilesize(fileURL);
		
		assertEquals(expectedSize, dbSize);
	}
	
	public void testDownloadFile() {
		String sourceURL = "http://www.google.ca/intl/en_ca/images/logo.gif";
		String targetPath = "src/com/amplio/rdict/tests/setup/word.db";
		
		DownloadManager downloadMgr = new DownloadManager();
		
		boolean downloadResult = downloadMgr.downloadFile(sourceURL, targetPath);
		
		File downloadedFile = new File(targetPath);
		
		assertTrue(downloadResult);
		assertTrue(downloadedFile.exists());
		assertEquals(8914, downloadedFile.length());
		assertEquals(8914, downloadMgr.tot_bytes_downloaded);
	}
	
	public void testGetProgress() {
		DownloadManager downloadMgr = new DownloadManager();
		
		downloadMgr.download_file_length = 500;
		downloadMgr.tot_bytes_downloaded = 0;
		
		assertEquals(0, downloadMgr.getProgress());
		
		downloadMgr.tot_bytes_downloaded = 50;
		
		assertEquals(10, downloadMgr.getProgress());
		
		downloadMgr.tot_bytes_downloaded = 500;
		
		assertEquals(100, downloadMgr.getProgress());
	}
	
	public void testStartDownload() {
		String sourceURL = "http://www.google.ca/intl/en_ca/images/logo.gif";
		String targetPath = "src/com/amplio/rdict/tests/setup/word.db";
		
		DownloadManager downloadMgr = new DownloadManager();
		downloadMgr.startDownload(sourceURL, targetPath, null, null);
		
		while(downloadMgr.isDownloading());
		
		File downloadedFile = new File(targetPath);
		
		assertTrue(downloadedFile.exists());
		assertEquals(8914, downloadedFile.length());
		assertEquals(8914, downloadMgr.tot_bytes_downloaded);
	}

}
