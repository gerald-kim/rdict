package com.amplio.rdict.setup;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.amplio.rdict.R;
import com.amplio.rdict.review.FlashCardActivity;

public class SetupActivity extends Activity {
	LinearLayout layout = null;
	
	SetupViewWrapper welcomeScreenViewWrapper;
	SetupViewWrapper promptForDBDownloadViewWrapper;
	DBDownloadingViewWrapper downloadingDBViewWrapper;
	SetupViewWrapper downloadFinishedViewWrapper;
	SetupViewWrapper downloadLaterViewWrapper;
	SetupViewWrapper downloadIsCorruptedViewWrapper;
	
	public static SetupManager setupMgr = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		
		this.layout = (LinearLayout) findViewById(R.id.setup_layout);
		
		this.welcomeScreenViewWrapper = new WelcomeViewWrapper(this.getApplicationContext(), this);
		
		this.promptForDBDownloadViewWrapper = new PromptForDBDownloadViewWrapper(this.getApplicationContext(), this);

		this.downloadingDBViewWrapper = new DBDownloadingViewWrapper(this.getApplicationContext(), this);
		this.downloadFinishedViewWrapper = new DBDownloadFinishedViewWrapper(this.getApplicationContext(), this);
		
		this.downloadLaterViewWrapper = new DownloadDBLaterViewWrapper(this.getApplicationContext(), this);
		this.downloadIsCorruptedViewWrapper = new DownloadIsCorruptedViewWrapper(this.getApplicationContext(), this);
		
		setupMgr = new SetupManager();
		
		if(null != savedInstanceState && savedInstanceState.containsKey(FlashCardActivity.SAVE_TAG_STATE)) {
			setupMgr.setState(savedInstanceState.getInt(FlashCardActivity.SAVE_TAG_STATE));
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(FlashCardActivity.SAVE_TAG_STATE, setupMgr.getState());
		super.onSaveInstanceState(outState);
	}
	
	public void onResume() {
		super.onResume();
		this.updateLayout();
	}
	
	public void updateLayout() {
		SetupViewWrapper wrapper = this.getViewByState(SetupActivity.setupMgr.getState());
		
		if(null != wrapper) {
			this.layout.removeAllViews();
			wrapper.getView().setAnimation(AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.fade));
			this.layout.addView(wrapper.getView());
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
			case SetupManager.STATE_VERIFYING:
				return this.downloadingDBViewWrapper;
			case SetupManager.STATE_VERIFICATION_COMPLETED:
				return this.downloadFinishedViewWrapper;
			case SetupManager.STATE_SETUP_COMPLETED:
				this.finish();
				return null;
			case SetupManager.STATE_DOWNLOAD_LATER:
				return this.downloadLaterViewWrapper;
			case SetupManager.STATE_PROMPT_DOWNLOAD_FOR_V_FAILURE:
				return this.downloadIsCorruptedViewWrapper;
			case SetupManager.STATE_SETUP_DELAYED:
				this.finish();
				return null;
			default:
				throw new IllegalArgumentException("No matching view for state: " + state);
		}
	}
	
	@Override
	public void onDestroy(){
		System.out.println("SetupActivity - OnDestroy");
		
		DownloadService.stop();
		
		File dicFile = new File(DictionaryDownloader.WRITE_PATH_DB);
		File indexFile = new File(DictionaryDownloader.WRITE_PATH_INDEX);
		
		if(dicFile.exists())
			dicFile.delete();
		
		if(indexFile.exists())
			indexFile.delete();
		
		super.onDestroy();
	}
}
