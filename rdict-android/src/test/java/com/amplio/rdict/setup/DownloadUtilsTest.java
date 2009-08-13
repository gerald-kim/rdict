package com.amplio.rdict.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

public class DownloadUtilsTest extends TestCase {
	private static final String TARGET_PATH = "tmp.db";
	private static final String FILE_URL = "http://www.google.ca/intl/en_ca/images/logo.gif";
	private static final String MD5_TEST_FILE_PATH = "src/test/java/com/amplio/rdict/setup/word.index.md5";
	
	public void tearDown() {
		File downloadedFile = new File( TARGET_PATH );
		downloadedFile.delete();
		
		File md5TestFile = new File(MD5_TEST_FILE_PATH);
		md5TestFile.delete();

		assertTrue( !downloadedFile.exists() );
		assertTrue( !md5TestFile.exists() );
	}
	
	public void testGetRemoteFileSize() {
		int expectedSize = 8914;

		int dbSize = DownloadUtils.getRemoteFilesize( FILE_URL );

		assertEquals( expectedSize, dbSize );
	}

	public void testDownloadFile() {
		boolean downloadResult = DownloadUtils.downloadFile( FILE_URL, TARGET_PATH, null);

		File downloadedFile = new File( TARGET_PATH );
		
		assertTrue( downloadResult );
		assertTrue( downloadedFile.exists() );
		assertEquals( 8914, downloadedFile.length() );
	}
	
	public void testDownloadFileWithDownloadMonitor() {
		DownloadMonitor downloadMonitor = new DownloadMonitor(null, null);

		boolean downloadResult = DownloadUtils.downloadFile(FILE_URL, TARGET_PATH, downloadMonitor);

		File downloadedFile = new File( TARGET_PATH );

		assertTrue( downloadResult );
		assertTrue( downloadedFile.exists() );
		assertEquals( 8914, downloadedFile.length() );
		assertEquals( 8914, downloadMonitor.m_bytesDownloaded );
	}
	
	public void testCalculateMd5Hash() {
		String md5HashFromPython = "44571ebaee6c4953be207219e7010c97";
		
		File f = new File("src/test/java/com/amplio/rdict/setup/word.index");
		
		byte[] md5Hash = DownloadUtils.calcMd5Hash(f);
		
		assertEquals(md5HashFromPython, DownloadUtils.getHexString(md5Hash));
	}
	
	public void testGetHexString() {
		// 0 + number is base 8, aka octal
		byte[] bytes = new byte[]{011, 021, 011, 011, 001, 02};
		
		assertEquals("091109090102", DownloadUtils.getHexString(bytes));
	}
	
	public void testReadMd5File() {
		byte[] md5Hash = DownloadUtils.calcMd5Hash(new File("src/test/java/com/amplio/rdict/setup/word.index"));
		writeMd5HashFile(md5Hash, new File(MD5_TEST_FILE_PATH));
		
		String md5AsString = DownloadUtils.readMd5File(new File(MD5_TEST_FILE_PATH));
		
		assertEquals(DownloadUtils.getHexString(md5Hash), md5AsString);
	}

	public void testCheckFileIntegrity() {
		File downloadedFile = new File("src/test/java/com/amplio/rdict/setup/word.index");
		File serversideMd5File = new File(MD5_TEST_FILE_PATH);
		
		byte[] md5Hash = DownloadUtils.calcMd5Hash(downloadedFile);
		writeMd5HashFile(md5Hash, serversideMd5File);
		
		boolean isValid = DownloadUtils.checkFileIntegrity(downloadedFile, serversideMd5File);

		assertTrue(isValid);
	}
	
	private void writeMd5HashFile( byte[] md5Hash, File md5TestFile) {
	    try {
			String hexString = DownloadUtils.getHexString(md5Hash);
			
	    	FileOutputStream fos = new FileOutputStream(md5TestFile);
			fos.write(hexString.getBytes());
			fos.close();
		} catch( FileNotFoundException e ) {
	        e.printStackTrace();
        } catch( IOException e ) {
	        e.printStackTrace();
        }
    }
}
