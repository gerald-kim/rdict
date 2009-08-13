package com.amplio.rdict.setup;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amplio.rdict.R;

public class DBDownloadingViewWrapper extends SetupViewWrapper {
	private TextView m_statusLabel = null;
	private ProgressBar downloadBar = null;
	private Button okButton = null;
	
	private Handler downloadingViewHandler = null;
	
	DownloadMonitor m_downloadMonitor = null;
	DictionaryDownloader m_downloader = null;
	
	public DBDownloadingViewWrapper(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		
		this.downloadingViewHandler = new Handler();
		
		this.v = View.inflate( context, R.layout.setup_downloading, null);
		
		this.m_statusLabel = (TextView) this.v.findViewById(R.id.setup_downloading_status_label );
		
		this.downloadBar = (ProgressBar) this.v.findViewById(R.id.setup_progress_bar );
		this.downloadBar.setProgress(0);
		
		this.okButton = (Button) this.v.findViewById(R.id.setup_download_finished_button );
		this.okButton.setVisibility(View.INVISIBLE);
		
		DownloadList downloadList = new DownloadList();
		downloadList.add(DictionaryDownloader.SOURCE_URL_DB, DictionaryDownloader.WRITE_PATH_DB);
		downloadList.add(DictionaryDownloader.SOURCE_URL_INDEX, DictionaryDownloader.WRITE_PATH_INDEX);
		
		m_downloadMonitor = new DownloadMonitor(this.downloadingViewHandler, this.getDownloadRunnable());
		
		m_downloader = new DictionaryDownloader(downloadList, m_downloadMonitor, DictionaryDownloader.DO_MD5_CHECK);
	}

	public void downloadAndCheckIndexAndDB() {
		m_downloader.start();
	}

	private Runnable getDownloadRunnable() {
		Runnable updateRunner = new Runnable() {
            public void run() {
                if( m_downloadMonitor.isDownloading()){
                	downloadBar.setProgress(m_downloadMonitor.getProgress());
                }
                else if( m_downloadMonitor.isVerifying()) {
                	SetupActivity.setupMgr.downloadCompleted();
                	
                	m_statusLabel.setText("Verifying...");
                }
                else if(m_downloadMonitor.isFinished()) {
                	if(DownloadMonitor.STATE_FINISHED_CHECKING_SUCCESS == m_downloadMonitor.getState()) {
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
