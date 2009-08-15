package com.amplio.rdict.setup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service implements Runnable{
	
	public static int n = 0;
	
	public static DownloadMonitor dm = null;

	public static boolean isRunning = false;
	
	public DictionaryDownloader m_downloader = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		DownloadList downloadList = new DownloadList();
		//downloadList.add(DictionaryDownloader.SOURCE_URL_DB, DictionaryDownloader.WRITE_PATH_DB);
		downloadList.add(DictionaryDownloader.SOURCE_URL_INDEX, DictionaryDownloader.WRITE_PATH_INDEX);
		
		m_downloader = new DictionaryDownloader(downloadList, dm, DictionaryDownloader.DO_MD5_CHECK);
		
		new Thread(this).start();
	}
	
	public void run() {
		isRunning = true;
		m_downloader.run();
		
		this.stopSelf();
		
		isRunning = false;
	}
	
	@Override
    public IBinder onBind( Intent arg0 ) {
	    // TODO Auto-generated method stub
	    return null;
    }

}