package com.amplio.rdict.setup;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amplio.rdict.R;

public class DBDownloadingViewWrapper extends SetupViewWrapper {
	private TextView m_statusLabel = null;
	private ProgressBar m_downloadBar = null;
	
	public Handler downloadingViewHandler = new Handler();
	
	public DBDownloadingViewWrapper(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		this.v = View.inflate(context, R.layout.setup_downloading, null);
		
		this.m_statusLabel = (TextView) this.v.findViewById(R.id.setup_downloading_status_label );
		
		this.m_downloadBar = (ProgressBar) this.v.findViewById(R.id.setup_progress_bar );
		
		if(DownloadService.isRunning) {		
			DownloadService.dm.m_handler = this.downloadingViewHandler;
			DownloadService.dm.m_runnable = this.getDownloadRunnable();
			this.m_downloadBar.setProgress(DownloadService.dm.getProgress());	
		}
		else {
			this.m_downloadBar.setProgress(0);
		}	
	}

	Runnable getDownloadRunnable() {
		Runnable updateRunner = new Runnable() {
            public void run() {
                if( DownloadService.dm.isDownloading()){
                	m_downloadBar.setProgress(DownloadService.dm.getProgress());
                }
                else if( DownloadService.dm.isVerifying()) {
                	SetupActivity.setupMgr.downloadCompleted();
                	
                	m_statusLabel.setText("Verifying...");
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
