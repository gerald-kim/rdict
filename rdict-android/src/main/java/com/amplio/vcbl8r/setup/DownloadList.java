package com.amplio.vcbl8r.setup;

import java.util.Vector;

public class DownloadList {

	Vector<DownloadFile> downloadList = null;
	
	public DownloadList() {
		downloadList= new Vector<DownloadFile>();
	}
	
	public void add( String srcFileUrl, String writePath ) {
		downloadList.add(new DownloadFile(srcFileUrl, writePath));
    }

	public int size() {
	    return downloadList.size();
    }

	public DownloadFile get( int i ) {
	    return downloadList.get(i);
    }

}
