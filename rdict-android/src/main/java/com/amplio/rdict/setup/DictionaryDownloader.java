package com.amplio.rdict.setup;

import java.io.File;

public class DictionaryDownloader implements Runnable {
    public static final String SOURCE_URL_DB = "http://s3.amazonaws.com/rdict-small/word.cdb";
    public static final String WRITE_PATH_DB = "/sdcard/rdict/word.cdb";
	
    public static final String SOURCE_URL_INDEX = "http://s3.amazonaws.com/rdict-small/word.index";
    public static final String WRITE_PATH_INDEX = "/sdcard/rdict/word.index";
    
	public static final boolean DO_MD5_CHECK = true;
	
	DownloadList downloadList = null;
	DownloadMonitor monitor = null;
	
	boolean doMd5Check = false;
	
	public DictionaryDownloader(DownloadList downloadList, DownloadMonitor downloadMonitor, boolean doMd5Check) {
		this.downloadList = downloadList;
		this.monitor = downloadMonitor;
		this.doMd5Check = doMd5Check;
    }
	
	public void start() {
		new Thread(this).start();
    }
	
	public void run() {
		if(isRunningInAndroidContext()){
			File file = new File("sdcard/rdict");
			file.mkdir();
		}
		
		this.monitor.setState(DownloadMonitor.STATE_DOWNLOADING_FILE_SIZES);		
		
		fetchFileSizes();
		
		if(isRunningInAndroidContext())
			this.monitor.postChange();
			
		this.monitor.setState(DownloadMonitor.STATE_DOWNLOADING_FILES);
		
		downloadFiles();
		
		if(isRunningInAndroidContext())
			this.monitor.postChange();
		
		if(this.doMd5Check) {
			this.monitor.setState(DownloadMonitor.STATE_VERIFYING_FILES);
			
			if(isRunningInAndroidContext())
				this.monitor.postChange();
			
			doMd5Check();
			
			if(isRunningInAndroidContext())
				this.monitor.postChange();
		}
		else {
			this.monitor.setState(DownloadMonitor.STATE_FINISHED_DOWNLOAD_ONLY);
			
			if(isRunningInAndroidContext())
				this.monitor.postChange();
		}
	}

	private void fetchFileSizes() {
	    for(int i = 0; i < downloadList.size(); i++) {
			this.monitor.numBytesToDownload += DownloadUtils.getRemoteFilesize(downloadList.get(i).srcFileUrl);
		
			if(this.doMd5Check) {
				this.monitor.numBytesToDownload += DownloadUtils.getRemoteFilesize(downloadList.get(i).md5FileUrl);
			}
		}
    }
	
	private void downloadFiles() {
	    for(int i = 0; i < this.downloadList.size(); i++) {
			DownloadUtils.downloadFile(downloadList.get(i).srcFileUrl, downloadList.get(i).writePath, this.monitor);
			
			if(this.doMd5Check) {
				DownloadUtils.downloadFile(downloadList.get(i).md5FileUrl, downloadList.get(i).md5FileWritePath, this.monitor);
			}
		}
    }
	
	private void doMd5Check() {
	    for(int i = 0; i < downloadList.size(); i++) {	
	    	File downloadedFile = new File(this.downloadList.get(i).writePath);
	    	File md5File = new File(this.downloadList.get(i).md5FileWritePath);
	    	
	    	if (! DownloadUtils.checkFileIntegrity(downloadedFile, md5File)) {
	    		this.monitor.setState(DownloadMonitor.STATE_FINISHED_CHECKING_FAILED);
	    		this.deleteAllFiles();
	    		return;
	    	}
	    }
	    
	    this.monitor.setState(DownloadMonitor.STATE_FINISHED_CHECKING_SUCCESS);
    }
	
	private void deleteAllFiles() {
	    for(int i = 0; i < this.downloadList.size(); i++) {
			new File(downloadList.get(i).writePath).delete();
			
			if(this.doMd5Check) {
				new File(downloadList.get(i).md5FileWritePath).delete();
			}
		}
    }
	
	private boolean isRunningInAndroidContext() {
	    return this.monitor.handler != null && this.monitor.runnable != null;
    }
}
