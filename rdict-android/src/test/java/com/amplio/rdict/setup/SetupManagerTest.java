package com.amplio.rdict.setup;

import com.amplio.rdict.setup.SetupManager;

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
		
		while(SetupManager.STATE_DOWNLOAD_FINSHED != setupMgr.getState());
		
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

}
