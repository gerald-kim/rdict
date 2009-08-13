package com.amplio.rdict.setup;

import java.io.File;

public class DictionaryDownloader implements Runnable {
    public static final String SOURCE_URL_DB = "http://s3.amazonaws.com/rdict/word.cdb";
    public static final String WRITE_PATH_DB = "/sdcard/rdict/word.cdb";
	
    public static final String SOURCE_URL_INDEX = "http://s3.amazonaws.com/rdict/word.index";
    public static final String WRITE_PATH_INDEX = "/sdcard/rdict/word.index";
    
	public static final boolean DO_MD5_CHECK = true;
	
	DownloadList m_downloadList = null;
	DownloadMonitor m_monitor = null;
	
	boolean m_doMd5Check = false;
	
	public DictionaryDownloader(DownloadList downloadList, DownloadMonitor downloadMonitor, boolean doMd5Check) {
		this.m_downloadList = downloadList;
		this.m_monitor = downloadMonitor;
		this.m_doMd5Check = doMd5Check;
    }
	
	public void start() {
		new Thread(this).start();
    }
	
	public void run() {
		if(runningInAndroidContext()){
			File file = new File("sdcard/rdict");
			file.mkdir();
		}
		
		this.m_monitor.setState(DownloadMonitor.STATE_DOWNLOADING_FILE_SIZES);		
		
		fetchFileSizes();
		
		if(runningInAndroidContext())
			this.m_monitor.postChange();
			
		this.m_monitor.setState(DownloadMonitor.STATE_DOWNLOADING_FILES);
		
		downloadFiles();
		
		if(runningInAndroidContext())
			this.m_monitor.postChange();
		
		if(this.m_doMd5Check) {
			this.m_monitor.setState(DownloadMonitor.STATE_VERIFYING_FILES);
			
			if(runningInAndroidContext())
				this.m_monitor.postChange();
			
			doMd5Check();
			
			if(runningInAndroidContext())
				this.m_monitor.postChange();
		}
		else {
			this.m_monitor.setState(DownloadMonitor.STATE_FINISHED_DOWNLOAD_ONLY);
			
			if(runningInAndroidContext())
				this.m_monitor.postChange();
		}
	}

	private void fetchFileSizes() {
	    for(int i = 0; i < m_downloadList.size(); i++) {
			this.m_monitor.m_numBytesToDownload += DownloadUtils.getRemoteFilesize(m_downloadList.get(i).m_srcFileUrl);
		
			if(this.m_doMd5Check) {
				this.m_monitor.m_numBytesToDownload += DownloadUtils.getRemoteFilesize(m_downloadList.get(i).m_md5FileUrl);
			}
		}
    }
	
	private void downloadFiles() {
	    for(int i = 0; i < this.m_downloadList.size(); i++) {
			DownloadUtils.downloadFile(m_downloadList.get(i).m_srcFileUrl, m_downloadList.get(i).m_writePath, this.m_monitor);
			
			if(this.m_doMd5Check) {
				DownloadUtils.downloadFile(m_downloadList.get(i).m_md5FileUrl, m_downloadList.get(i).m_md5FileWritePath, this.m_monitor);
			}
		}
    }
	
	private void doMd5Check() {
	    for(int i = 0; i < m_downloadList.size(); i++) {	
	    	File downloadedFile = new File(this.m_downloadList.get(i).m_writePath);
	    	File md5File = new File(this.m_downloadList.get(i).m_md5FileWritePath);
	    	
	    	if (! DownloadUtils.checkFileIntegrity(downloadedFile, md5File)) {
	    		this.m_monitor.setState(DownloadMonitor.STATE_FINISHED_CHECKING_FAILED);
	    		return;
	    	}
	    }
	    
	    this.m_monitor.setState(DownloadMonitor.STATE_FINISHED_CHECKING_SUCCESS);
    }
	
	private boolean runningInAndroidContext() {
	    return this.m_monitor.m_handler != null && this.m_monitor.m_runnable != null;
    }
}
