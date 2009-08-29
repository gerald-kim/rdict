package com.amplio.rdict.setup;


public class SetupManager {
	public static final int STATE_WELCOME = 0;
	public static final int STATE_PROMPT_DOWNLOAD = 1;
	public static final int STATE_DOWNLOADING = 2;
	public static final int STATE_SETUP_COMPLETED = 3;
	public static final int STATE_DOWNLOAD_LATER = 4;
	public static final int STATE_SETUP_DELAYED = 5;
	public static final int STATE_VERIFYING = 6;
	public static final int STATE_VERIFICATION_COMPLETED = 7;
	public static final int STATE_PROMPT_DOWNLOAD_FOR_V_FAILURE = 8;

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

	public void downloadCompleted() {
		this.state = SetupManager.STATE_VERIFYING;
	}

	public void verificationWasSuccessful() {
	    this.state = STATE_VERIFICATION_COMPLETED;
    }

	public void verificationFailed() {
		this.state = STATE_PROMPT_DOWNLOAD_FOR_V_FAILURE;
    }
	
	public int getState() {
		return this.state;
	}

	public void setState(int state ) {
		this.state = state;
	}
}
