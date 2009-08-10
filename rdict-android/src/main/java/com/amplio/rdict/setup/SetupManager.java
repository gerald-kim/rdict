package com.amplio.rdict.setup;


public class SetupManager {

	public static final int STATE_WELCOME = 0;
	public static final int STATE_PROMPT_DOWNLOAD = 1;
	public static final int STATE_DOWNLOADING = 2;
	public static final int STATE_SETUP_COMPLETED = 3;
	public static final int STATE_DOWNLOAD_FINSHED = 4;
	public static final int STATE_DOWNLOAD_LATER = 5;
	public static final int STATE_SETUP_DELAYED = 6;
	public static final int STATE_VERIFYING = 7;
	public static final int STATE_VERIFICATION_COMPLETED = 8;
	public static final int STATE_PROMPT_DOWNLOAD_FOR_V_FAILURE = 9;

	private int m_state = STATE_WELCOME;
	
	public void userAcknowledgedWelcomeScreen() {
		this.m_state = STATE_PROMPT_DOWNLOAD;
	}

	public void userChoseDownloadOption() {
		this.m_state = STATE_DOWNLOADING;
	}

	public void userClickedFinish() {
		this.m_state = STATE_SETUP_COMPLETED;
	}

	public void userChoseToDelayDownlaod() {
		this.m_state = STATE_DOWNLOAD_LATER;
	}

	public void userAcknowledgedNeedToDownloadLater() {
		this.m_state = STATE_SETUP_DELAYED;
	}

	public int getState() {
		return this.m_state;
	}

	public void downloadCompleted() {
		this.m_state = SetupManager.STATE_VERIFYING;
	}

	public void verificationWasSuccessful() {
	    this.m_state = STATE_VERIFICATION_COMPLETED;
	    
    }

	public void verificationFailed() {
		this.m_state = STATE_PROMPT_DOWNLOAD_FOR_V_FAILURE;
    }
}
