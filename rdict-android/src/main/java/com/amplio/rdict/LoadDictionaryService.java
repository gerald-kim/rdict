package com.amplio.rdict;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.amplio.rdict.search.Dictionary;

public class LoadDictionaryService extends Service implements Runnable {
	public static boolean isRunning = false;
	
	public static SplashActivity splashActivity = null;

	@Override
	public void onCreate() {
		super.onCreate();
		new Thread(this).start();
	}
	
	public void run() {
		isRunning = true;
		
		RDictActivity.c_dictionary = new Dictionary( "/sdcard/rdict/word.cdb", "/sdcard/rdict/word.index",
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
	
}
