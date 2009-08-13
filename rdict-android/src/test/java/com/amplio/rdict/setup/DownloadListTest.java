package com.amplio.rdict.setup;

import junit.framework.TestCase;

public class DownloadListTest extends TestCase {

	public void testUsage() {
		String srcFileUrl = "the file url";
		String writePath = "/sdcard/file";
		
		DownloadList list = new DownloadList();
		
		list.add(srcFileUrl, writePath);
		list.add("another file", "another writepath");
		
		assertEquals(2, list.size());
		assertEquals(srcFileUrl, list.get(0).m_srcFileUrl);
		assertEquals(writePath, list.get(0).m_writePath);
	}

}
