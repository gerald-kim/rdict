package com.amplio.rdict;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.amplio.rdict.search.Dictionary;

public class LoadDictionaryService extends Service implements Runnable {
	public static boolean isRunning = false;
	
	public static SplashActivity splashActivity = null;
	
	private static LoadDictionaryService THIS = null;

	@Override
	public void onCreate() {
		super.onCreate();
		
		THIS = this;
		
		new Thread(this).start();
	}
	
	public void run() {
		isRunning = true;
		
		RDictActivity.dictionary = new Dictionary(); 
		RDictActivity.dictionary.load("/sdcard/rdict/word.cdb", "/sdcard/rdict/word.index",
		        						splashActivity.getAssetInputStream( "dictionary_js.html"), 
		        						splashActivity.getHandler(), 
		        						splashActivity.getRunnableForDBInit());
		
		stopSelf();
		
		isRunning = false;
		
		splashActivity.getHandler().post(splashActivity.getRunnableForDBInit());
    }
	
	@Override
    public IBinder onBind( Intent arg0 ) {
	    return null;
    }
	
	public static void stop(){
		if(THIS != null) {
			THIS.stopSelf();
			isRunning = false;
		}
	}
	
}
