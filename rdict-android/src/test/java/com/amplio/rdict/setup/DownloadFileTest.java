package com.amplio.rdict.setup;

import junit.framework.TestCase;

public class DownloadFileTest extends TestCase {
	
	public void testDownloadFile() {
		String srcFileUrl = "file";
		String writePath = "/sdcard/rdict/file";
		
		DownloadFile file = new DownloadFile(srcFileUrl, writePath);
		
		assertEquals(srcFileUrl, file.m_srcFileUrl);
		assertEquals(srcFileUrl + ".md5", file.m_md5FileUrl);
		
		assertEquals(writePath, file.m_writePath);
		assertEquals(writePath + ".md5", file.m_md5FileWritePath);
	}
}
