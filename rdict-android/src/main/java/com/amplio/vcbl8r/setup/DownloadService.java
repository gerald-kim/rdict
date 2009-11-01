package com.amplio.vcbl8r.setup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service implements Runnable{
	
	public static int n = 0;
	
	public static DownloadMonitor dm = null;

	public static boolean isRunning = false;
	
	public DictionaryDownloader downloader = null;
	
	private static DownloadService THIS = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		DownloadList downloadList = new DownloadList();
		downloadList.add(DictionaryDownloader.SOURCE_URL_DB, DictionaryDownloader.WRITE_PATH_DB);
		downloadList.add(DictionaryDownloader.SOURCE_URL_INDEX, DictionaryDownloader.WRITE_PATH_INDEX);
		
		downloader = new DictionaryDownloader(downloadList, dm, DictionaryDownloader.DO_MD5_CHECK);
		
		THIS = this;
		
		new Thread(this).start();
	}
	
	public void run() {
		isRunning = true;
		downloader.run();
		
		this.stopSelf();
		
		isRunning = false;
	}
	
	public static void stop(){
		if(THIS != null) {
			THIS.stopSelf();
			isRunning = false;
		}
	}
	
	@Override
    public IBinder onBind( Intent arg0 ) {
	    return null;
    }

}
