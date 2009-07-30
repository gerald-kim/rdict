package com.amplio.rdict.setup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.amplio.rdict.R;

public class SetupActivity extends Activity {
	LinearLayout layout = null;
	
	SetupView welcomeScreenView;
	SetupView promptForDBDownloadView;
	SetupView downloadingDBView;
	SetupView downloadFinishedView;
	SetupView downloadLaterView;
	
	public static SetupManager setupMgr = null;
	
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.setup);
		
		this.layout = (LinearLayout) findViewById(R.id.setup_layout);
		
		this.welcomeScreenView = new WelcomeView(this.getApplicationContext(), this);
		this.promptForDBDownloadView = new PromptForDBDownloadView(this.getApplicationContext(), this);

		this.downloadingDBView = new DBDownloadingView(this.getApplicationContext(), this);
		this.downloadFinishedView = new DBDownloadFinishedView(this.getApplicationContext(), this);
		
		this.downloadLaterView = new DownloadDBLaterView(this.getApplicationContext(), this);
	}
	
	public void onResume() {
		super.onResume();
		this.updateLayout();
	}
	
	public void updateLayout() {
		SetupView view = this.getViewByState(SetupActivity.setupMgr.getState());
		
		if(null != view) {
			this.layout.removeAllViews();			
			view.addToLayout(this.layout);
		
			if(SetupManager.STATE_DOWNLOADING == SetupActivity.setupMgr.getState()) {
				((DBDownloadingView) view).startDownload();
			}
		}
	}
	
	private SetupView getViewByState(int state) {
		switch(state){
			case SetupManager.STATE_WELCOME:
				return this.welcomeScreenView;
			case SetupManager.STATE_PROMPT_DOWNLOAD:
				return this.promptForDBDownloadView;
			case SetupManager.STATE_DOWNLOADING:
				return this.downloadingDBView;
			case SetupManager.STATE_DOWNLOAD_FINSHED:
				return this.downloadFinishedView;
			case SetupManager.STATE_SETUP_COMPLETED:
				this.finish();
				return null;
			case SetupManager.STATE_DOWNLOAD_LATER:
				return this.downloadLaterView;
			case SetupManager.STATE_SETUP_DELAYED:
				this.finish();
				return null;
			default:
				throw new IllegalArgumentException("No matching view for state: " + state);
		}
	}
}
