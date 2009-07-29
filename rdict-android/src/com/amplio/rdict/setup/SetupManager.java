package com.amplio.rdict.setup;


public class SetupManager {

	public static final int STATE_WELCOME = 0;
	public static final int STATE_PROMPT_DOWNLOAD = 1;
	public static final int STATE_DOWNLOADING = 2;
	public static final int STATE_SETUP_COMPLETED = 3;
	public static final int STATE_DOWNLOAD_FINSHED = 4;
	public static final int STATE_DOWNLOAD_LATER = 5;
	public static final int STATE_SETUP_DELAYED = 6;

	private int state = STATE_WELCOME;
	
	public void userAcknowledgedWelcomeScreen() {
		this.state = STATE_PROMPT_DOWNLOAD;
	}

	public void userChoseDownloadOption() {
		this.state = STATE_DOWNLOADING;
	}

	public void userClickedFinish() {
		this.state = STATE_SETUP_COMPLETED;
	}

	public void userChoseToDelayDownlaod() {
		this.state = STATE_DOWNLOAD_LATER;
	}

	public void userAcknowledgedNeedToDownloadLater() {
		this.state = STATE_SETUP_DELAYED;
	}

	public int getState() {
		return this.state;
	}

	public void downloadCompleted() {
		state = SetupManager.STATE_DOWNLOAD_FINSHED;
	}
}
