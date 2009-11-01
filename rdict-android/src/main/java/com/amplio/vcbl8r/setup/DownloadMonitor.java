package com.amplio.vcbl8r.setup;

import android.os.Handler;

public class DownloadMonitor {
	public static final int STATE_NOT_STARTED = 0;
	public static final int STATE_DOWNLOADING_FILE_SIZES = 1;
	public static final int STATE_DOWNLOADING_FILES = 2;
	public static final int STATE_FINISHED_DOWNLOAD_ONLY = 3;
	public static final int STATE_VERIFYING_FILES = 4;
	public static final int STATE_FINISHED_CHECKING_SUCCESS = 5;
	public static final int STATE_FINISHED_CHECKING_FAILED = 6;
	
	private int state = STATE_NOT_STARTED;
	
	Handler handler = null;
	Runnable runnable = null;
	
	public long numBytesToDownload = 0;
	public long bytesDownloaded = 0;
	
	public DownloadMonitor(Handler handler, Runnable runnable) {
		this.handler = handler;
		this.runnable = runnable;					
	}
	
	public void postChange() {
		this.handler.post(this.runnable);
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public boolean isDownloading() {
		return this.state == STATE_DOWNLOADING_FILE_SIZES || this.state == STATE_DOWNLOADING_FILES;
	}
	
	public boolean isVerifying() {
		return this.state == STATE_VERIFYING_FILES;
    }
	
	public int getProgress() {
		return new Double((new Long(this.bytesDownloaded).doubleValue() / this.numBytesToDownload) * 100).intValue();
	}

	public boolean isFinished() {
		return this.state == STATE_FINISHED_DOWNLOAD_ONLY
				|| this.state == STATE_FINISHED_CHECKING_FAILED
				|| this.state == STATE_FINISHED_CHECKING_SUCCESS;
    }
	
	public int getState() {
		return this.state;
	}
}
