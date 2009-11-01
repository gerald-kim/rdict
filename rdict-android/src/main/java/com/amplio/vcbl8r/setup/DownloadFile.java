package com.amplio.vcbl8r.setup;

public class DownloadFile {
	public String srcFileUrl = null;
	public String md5FileUrl = null;
	public String writePath = null;
	public String md5FileWritePath = null;
	
	public DownloadFile( String srcFileUrl, String writePath) {
		this.srcFileUrl = srcFileUrl;
		md5FileUrl = srcFileUrl + ".md5";
		
		this.writePath = writePath;
		md5FileWritePath = writePath + ".md5";
    }
}
