package com.amplio.rdict.setup;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amplio.rdict.R;

public class DBDownloadingViewWrapper extends SetupViewWrapper {
	private TextView statusLabel = null;
	private ProgressBar downloadBar = null;
	
	public Handler downloadingViewHandler = null;
	
	public DBDownloadingViewWrapper(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		this.v = View.inflate(context, R.layout.setup_downloading, null);
		
		this.statusLabel = (TextView) this.v.findViewById(R.id.setup_downloading_status_label );
		
		this.downloadingViewHandler = new Handler();
		
		this.downloadBar = (ProgressBar) this.v.findViewById(R.id.setup_progress_bar );
		
		if(DownloadService.isRunning) {		
			DownloadService.dm.handler = this.downloadingViewHandler;
			DownloadService.dm.runnable = this.getDownloadRunnable();
			this.downloadBar.setProgress(DownloadService.dm.getProgress());
			
			if(DownloadService.dm.isVerifying()) {		
				this.statusLabel.setText("Verifying...");
			}
		}
		else {
			this.downloadBar.setProgress(0);
		}	
	}

	Runnable getDownloadRunnable() {
		Runnable updateRunner = new Runnable() {
            public void run() {
                if( DownloadService.dm.isDownloading()){
                	downloadBar.setProgress(DownloadService.dm.getProgress());
                }
                else if( DownloadService.dm.isVerifying()) {
                	SetupActivity.setupMgr.downloadCompleted();
                	
                	statusLabel.setText("Verifying...");
                }
                else if(DownloadService.dm.isFinished()) {
                	if(DownloadMonitor.STATE_FINISHED_CHECKING_SUCCESS == DownloadService.dm.getState()) {
                		SetupActivity.setupMgr.verificationWasSuccessful();
                	}
                	else {
                		SetupActivity.setupMgr.verificationFailed();
                	}
                	
            		setupActivity.updateLayout();
                }
            }
		};
		
		return updateRunner;
	}
}
