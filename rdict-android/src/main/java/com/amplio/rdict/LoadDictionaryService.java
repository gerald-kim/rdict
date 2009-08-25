package com.amplio.rdict;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.amplio.rdict.search.Dictionary;

public class LoadDictionaryService extends Service implements Runnable {
	public static boolean isRunning = false;
	
	public static SplashActivity m_a = null;

	@Override
	public void onCreate() {
		super.onCreate();
		new Thread(this).start();
	}
	
	public void run() {
		isRunning = true;
		
		RDictActivity.c_dictionary = new Dictionary( "/sdcard/rdict/word.cdb", "/sdcard/rdict/word.index",
					        						m_a.getAssetInputStream( "dictionary_js.html"), 
					        						m_a.getHandler(), 
					        						m_a.getRunnableForDBInit());
		
		this.stopSelf();
		
		isRunning = false;
		
		m_a.getHandler().post(m_a.getRunnableForDBInit());
    }
	
	@Override
    public IBinder onBind( Intent arg0 ) {
	    return null;
    }
	
}
