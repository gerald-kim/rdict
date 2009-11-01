package com.amplio.vcbl8r;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.amplio.vcbl8r.R;
import com.amplio.vcbl8r.search.Dictionary;

public class SplashActivity extends Activity {
	private ProgressBar downloadBar = null;
	
	private Handler handler = null;
	
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		
		setContentView(R.layout.splash_screen);
		
		downloadBar = (ProgressBar) findViewById(R.id.init_managers_bar);
		handler = new Handler();
		
		LoadDictionaryService.splashActivity = this;
		
		if(! LoadDictionaryService.isRunning)
			startService(new Intent(this, LoadDictionaryService.class));
	}
	
	public Handler getHandler() {
	    return handler;
    }
	
	public Runnable getRunnableForDBInit() {
		return new Runnable() {
			public void run() {
				if(LoadDictionaryService.isRunning) {
					downloadBar.setProgress(Dictionary.getProgress());
				}
				else {
					RDictActivity.didLoadDict = true;
					finish();
				}
            }
		};		
	}
	
	public InputStream getAssetInputStream(String path) {
		return getAssetInputStream(this, path );
	}
	
	public static InputStream getAssetInputStream(Activity activity, String path) {
		InputStream stream = null;
		try {
			stream = activity.getAssets().open(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stream;
	}
}
