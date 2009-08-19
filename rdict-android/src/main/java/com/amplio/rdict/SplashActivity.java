package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;

import com.amplio.rdict.search.Dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {
	
	private ProgressBar m_downloadBar = null;
	
	protected Handler m_handler = null;
	
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		
		setContentView(R.layout.splash_screen);
		
		this.m_downloadBar = (ProgressBar) this.findViewById(R.id.init_managers_bar);
		this.m_handler = new Handler();
		
		LoadDictionaryService.m_a = this;
		this.startService(new Intent(this, LoadDictionaryService.class));
	}
	
	public Runnable getRunnableForDBInit() {
		return new Runnable() {
			public void run() {
				if(LoadDictionaryService.isRunning) {
					m_downloadBar.setProgress(Dictionary.getProgress());
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
