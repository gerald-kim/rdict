package com.amplio.rdict.setup;

import java.util.Vector;

public class DownloadList {

	Vector<DownloadFile> m_downloadList = null;
	
	public DownloadList() {
		m_downloadList= new Vector<DownloadFile>();
	}
	
	public void add( String srcFileUrl, String writePath ) {
		m_downloadList.add(new DownloadFile(srcFileUrl, writePath));
    }

	public int size() {
	    return m_downloadList.size();
    }

	public DownloadFile get( int i ) {
	    return m_downloadList.get(i);
    }

}
