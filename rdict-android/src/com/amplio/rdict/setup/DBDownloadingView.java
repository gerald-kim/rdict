package com.amplio.vcbl8r.setup;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DBDownloadingView extends SetupView {
	TextView downloadingLabel = null;
	ProgressBar downloadBar = null;
	Button okButton = null;
	
	Handler progresBarUpdateHandler = new Handler();	
	int mProgressStatus = 0;
	
	DownloadManager dMgr = null;
	
	String sourceURL =  "http://www.killer-rabbits.net/word.db"; //"http://www.google.ca/intl/en_ca/images/logo.gif";
	String writePath = "/sdcard/vcbl8r/word.db";
	
	Handler mHandler = new Handler();
	
	public DBDownloadingView(Context context) {
		super(context);
		
		this.downloadingLabel = new TextView(context);
		this.downloadingLabel.setText("Downloading...");
		
		this.okButton = new Button(context);
		this.okButton.setText("Ok");
		this.setOnClickListener(this);
		this.okButton.setVisibility(View.INVISIBLE);
		
		downloadBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		downloadBar.setProgress(0);
		
    	dMgr = new DownloadManager();
	}

	@Override
	public void addToLayout(LinearLayout l) {
		l.addView(this.downloadingLabel);
		l.addView(this.downloadBar);
		l.addView(this.okButton);
	}

	public void onClick(View v) {
	}
	
	public void startDownload() {
		dMgr.startDownload(this.sourceURL, this.writePath, mHandler, this.getRunnable());
	}

	private Runnable getRunnable() {
		Runnable updateRunner = new Runnable() {
            public void run() {
                downloadBar.setProgress(dMgr.getProgress());
                
                if(! dMgr.isDownloading()){
                	okButton.setVisibility(View.VISIBLE);
                	
                	setupMgr.downloadCompleted();
            		sa.updateLayout();
                }
            }
		};
		
		return updateRunner;
	}
}
