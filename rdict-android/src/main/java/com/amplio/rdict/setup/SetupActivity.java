package com.amplio.rdict.setup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.amplio.rdict.R;

public class SetupActivity extends Activity {
	LinearLayout layout = null;
	
	SetupViewWrapper welcomeScreenViewWrapper;
	SetupViewWrapper promptForDBDownloadViewWrapper;
	SetupViewWrapper downloadingDBViewWrapper;
	SetupViewWrapper downloadFinishedViewWrapper;
	SetupViewWrapper downloadLaterViewWrapper;
	
	public static SetupManager setupMgr = null;
	
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.setup);
		
		this.layout = (LinearLayout) findViewById(R.id.setup_layout);
		
		this.welcomeScreenViewWrapper = new WelcomeViewWrapper(this.getApplicationContext(), this);
		
		this.promptForDBDownloadViewWrapper = new PromptForDBDownloadViewWrapper(this.getApplicationContext(), this);

		this.downloadingDBViewWrapper = new DBDownloadingViewWrapper(this.getApplicationContext(), this);
		this.downloadFinishedViewWrapper = new DBDownloadFinishedViewWrapper(this.getApplicationContext(), this);
		
		this.downloadLaterViewWrapper = new DownloadDBLaterViewWrapper(this.getApplicationContext(), this);
	}
	
	public void onResume() {
		super.onResume();
		this.updateLayout();
	}
	
	public void updateLayout() {
		SetupViewWrapper wrapper = this.getViewByState(SetupActivity.setupMgr.getState());
		
		if(null != wrapper) {
			this.layout.removeAllViews();			
			this.layout.addView(wrapper.getView());
		
			if(SetupManager.STATE_DOWNLOADING == SetupActivity.setupMgr.getState()) {
				((DBDownloadingViewWrapper) wrapper).startDownload();			
			}
		}
	}
	
	private SetupViewWrapper getViewByState(int state) {
		switch(state){
			case SetupManager.STATE_WELCOME:
				return this.welcomeScreenViewWrapper;
			case SetupManager.STATE_PROMPT_DOWNLOAD:
				return this.promptForDBDownloadViewWrapper;
			case SetupManager.STATE_DOWNLOADING:
				return this.downloadingDBViewWrapper;
			case SetupManager.STATE_DOWNLOAD_FINSHED:
				return this.downloadFinishedViewWrapper;
			case SetupManager.STATE_SETUP_COMPLETED:
				this.finish();
				return null;
			case SetupManager.STATE_DOWNLOAD_LATER:
				return this.downloadLaterViewWrapper;
			case SetupManager.STATE_SETUP_DELAYED:
				this.finish();
				return null;
			default:
				throw new IllegalArgumentException("No matching view for state: " + state);
		}
	}
}
