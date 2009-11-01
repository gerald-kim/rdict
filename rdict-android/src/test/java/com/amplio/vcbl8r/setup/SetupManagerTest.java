package com.amplio.vcbl8r.setup;

import com.amplio.vcbl8r.setup.SetupManager;

import junit.framework.TestCase;

public class SetupManagerTest extends TestCase {
	
	public void testCycle() {
		SetupManager setupMgr = new SetupManager();
		
		assertEquals(SetupManager.STATE_WELCOME, setupMgr.getState());
		
		setupMgr.userAcknowledgedWelcomeScreen();
		
		assertEquals(SetupManager.STATE_PROMPT_DOWNLOAD, setupMgr.getState());
		
		setupMgr.userChoseDownloadOption();
		
		assertEquals(SetupManager.STATE_DOWNLOADING, setupMgr.getState());
		
		setupMgr.downloadCompleted();
		
		assertEquals(SetupManager.STATE_VERIFYING, setupMgr.getState());
		
		setupMgr.verificationWasSuccessful();
	
		assertEquals(SetupManager.STATE_VERIFICATION_COMPLETED, setupMgr.getState());
		
		setupMgr.userClickedFinish();
		
		assertEquals(SetupManager.STATE_SETUP_COMPLETED, setupMgr.getState());
	}
	
	public void testCycleForDelayDownload() {
		SetupManager setupMgr = new SetupManager();
		
		assertEquals(SetupManager.STATE_WELCOME, setupMgr.getState());
		
		setupMgr.userAcknowledgedWelcomeScreen();
		
		assertEquals(SetupManager.STATE_PROMPT_DOWNLOAD, setupMgr.getState());
		
		setupMgr.userChoseToDelayDownlaod();
		
		assertEquals(SetupManager.STATE_DOWNLOAD_LATER, setupMgr.getState());
		
		setupMgr.userAcknowledgedNeedToDownloadLater();
		
		assertEquals(SetupManager.STATE_SETUP_DELAYED, setupMgr.getState());
	}
	
	public void testCycleForCorruptedDownload() {
		SetupManager setupMgr = new SetupManager();
		
		setupMgr.userAcknowledgedWelcomeScreen();
		setupMgr.userChoseDownloadOption();
		setupMgr.downloadCompleted();
		
		assertEquals(SetupManager.STATE_VERIFYING, setupMgr.getState());
		
		setupMgr.verificationFailed();
	
		assertEquals(SetupManager.STATE_PROMPT_DOWNLOAD_FOR_V_FAILURE, setupMgr.getState());
		
		// reuse the download dialogs from here.
		
		setupMgr.userChoseDownloadOption();
		
		assertEquals(SetupManager.STATE_DOWNLOADING, setupMgr.getState());
		
		setupMgr.downloadCompleted();
		
		assertEquals(SetupManager.STATE_VERIFYING, setupMgr.getState());
		
		//... this time the download works.
	}

}
