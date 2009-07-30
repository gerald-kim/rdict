package com.amplio.rdict.setup;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DBDownloadingView extends SetupView {
	private TextView downloadingLabel = null;
	private Button okButton = null;
	private ProgressBar downloadBar = null;
	
	private Handler progresBarUpdateHandler = null;
	
	DownloadManager dMgr = null;
	
	public DBDownloadingView(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		
		this.downloadingLabel = new TextView(context);
		this.downloadingLabel.setText("Downloading...");
		
		this.okButton = new Button(context);
		this.okButton.setText("Ok");
		this.okButton.setVisibility(View.INVISIBLE);
		
		this.downloadBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		this.downloadBar.setProgress(0);
		
		this.progresBarUpdateHandler = new Handler();
		
    	dMgr = new DownloadManager();
	}

	@Override
	public void addToLayout(LinearLayout l) {
		l.addView(this.downloadingLabel);
		l.addView(this.downloadBar);
		l.addView(this.okButton);
	}
	
	public void startDownload() {
		dMgr.startDownload(DownloadManager.SOURCE_URL, DownloadManager.WRITE_PATH, progresBarUpdateHandler, this.getRunnable());
	}

	private Runnable getRunnable() {
		Runnable updateRunner = new Runnable() {
            public void run() {
                downloadBar.setProgress(dMgr.getProgress());
                
                if(! dMgr.isDownloading()){
                	okButton.setVisibility(View.VISIBLE);
                	
                	SetupActivity.setupMgr.downloadCompleted();
            		setupActivity.updateLayout();
                }
            }
		};
		
		return updateRunner;
	}
}
