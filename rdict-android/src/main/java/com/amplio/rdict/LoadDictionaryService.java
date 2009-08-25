package com.amplio.rdict;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.amplio.rdict.search.Dictionary;

public class LoadDictionaryService extends Service implements Runnable {
	public static boolean isRunning = false;
	
	public static SplashActivity m_splashActivity = null;

	@Override
	public void onCreate() {
		super.onCreate();
		new Thread(this).start();
	}
	
	public void run() {
		isRunning = true;
		
		RDictActivity.c_dictionary = new Dictionary( "/sdcard/rdict/word.cdb", "/sdcard/rdict/word.index",
					        						m_splashActivity.getAssetInputStream( "dictionary_js.html"), 
					        						m_splashActivity.getHandler(), 
					        						m_splashActivity.getRunnableForDBInit());
		
		stopSelf();
		
		isRunning = false;
		
		m_splashActivity.getHandler().post(m_splashActivity.getRunnableForDBInit());
    }
	
	@Override
    public IBinder onBind( Intent arg0 ) {
	    return null;
    }
	
}
