package com.amplio.rdict.setup;

public class DownloadFile {
	public String m_srcFileUrl = null;
	public String m_md5FileUrl = null;
	public String m_writePath = null;
	public String m_md5FileWritePath = null;
	
	public DownloadFile( String srcFileUrl, String writePath) {
		m_srcFileUrl = srcFileUrl;
		m_md5FileUrl = srcFileUrl + ".md5";
		
		m_writePath = writePath;
		m_md5FileWritePath = writePath + ".md5";
    }
}
