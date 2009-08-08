package com.amplio.rdict.setup;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.amplio.rdict.R;

public class DBDownloadingViewWrapper extends SetupViewWrapper {
	private ProgressBar downloadBar = null;
	private Button okButton = null;
	
	private Handler progresBarUpdateHandler = null;
	
	DownloadManager dMgr = null;
	
	public DBDownloadingViewWrapper(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		
		this.v = View.inflate( context, R.layout.setup_downloading, null);
		
		//new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		this.downloadBar = (ProgressBar) this.v.findViewById(R.id.setup_progress_bar );
		this.downloadBar.setProgress(0);
		
		this.okButton = (Button) this.v.findViewById(R.id.setup_download_finished_button );
		this.okButton.setVisibility(View.INVISIBLE);
		
		this.progresBarUpdateHandler = new Handler();
		
    	dMgr = new DownloadManager();
	}

	public void startDownload() {
		String[] sourceURLs = new String[]{DownloadManager.SOURCE_URL_DB, DownloadManager.SOURCE_URL_INDEX};
		String[] writePaths = new String[]{DownloadManager.WRITE_PATH_DB, DownloadManager.WRITE_PATH_INDEX};

		dMgr.startDownload(sourceURLs, writePaths, progresBarUpdateHandler, this.getRunnable());
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
