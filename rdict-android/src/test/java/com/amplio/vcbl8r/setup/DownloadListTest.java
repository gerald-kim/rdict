package com.amplio.vcbl8r.setup;

import com.amplio.vcbl8r.setup.DownloadList;

import junit.framework.TestCase;

public class DownloadListTest extends TestCase {

	public void testUsage() {
		String srcFileUrl = "the file url";
		String writePath = "/sdcard/file";
		
		DownloadList list = new DownloadList();
		
		list.add(srcFileUrl, writePath);
		list.add("another file", "another writepath");
		
		assertEquals(2, list.size());
		assertEquals(srcFileUrl, list.get(0).srcFileUrl);
		assertEquals(writePath, list.get(0).writePath);
	}

}
