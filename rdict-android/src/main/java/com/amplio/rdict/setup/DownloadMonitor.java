package com.amplio.rdict.setup;

import android.os.Handler;

public class DownloadMonitor {
	public static final int STATE_NOT_STARTED = 0;
	public static final int STATE_DOWNLOADING_FILE_SIZES = 1;
	public static final int STATE_DOWNLOADING_FILES = 2;
	public static final int STATE_FINISHED_DOWNLOAD_ONLY = 3;
	public static final int STATE_VERIFYING_FILES = 4;
	public static final int STATE_FINISHED_CHECKING_SUCCESS = 5;
	public static final int STATE_FINISHED_CHECKING_FAILED = 6;
	
	private int m_state = STATE_NOT_STARTED;
	
	Handler m_handler = null;
	Runnable m_runnable = null;
	
	public long m_numBytesToDownload = 0;
	public long m_bytesDownloaded = 0;
	
	public DownloadMonitor(Handler handler, Runnable runnable) {
		this.m_handler = handler;
		this.m_runnable = runnable;					
	}
	
	public void postChange() {
		this.m_handler.post(this.m_runnable);
	}
	
	public void setState(int state) {
		this.m_state = state;
	}
	
	public boolean isDownloading() {
		return this.m_state == STATE_DOWNLOADING_FILE_SIZES || this.m_state == STATE_DOWNLOADING_FILES;
	}
	
	public boolean isVerifying() {
		return this.m_state == STATE_VERIFYING_FILES;
    }
	
	public int getProgress() {
		return new Double((new Long(this.m_bytesDownloaded).doubleValue() / this.m_numBytesToDownload) * 100).intValue();
	}

	public boolean isFinished() {
		return this.m_state == STATE_FINISHED_DOWNLOAD_ONLY
				|| this.m_state == STATE_FINISHED_CHECKING_FAILED
				|| this.m_state == STATE_FINISHED_CHECKING_SUCCESS;
    }
	
	public int getState() {
		return this.m_state;
	}
}
