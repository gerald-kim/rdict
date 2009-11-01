package com.amplio.vcbl8r;

import com.amplio.vcbl8r.StartupManager;

import junit.framework.TestCase;

public class StartupManagerTest extends TestCase {
	
	public void testGetAction() {
		boolean existsNecessaryFiles = false;
		boolean didSetupRun = false;
		boolean didTryToLoadDict = false;
		boolean didLoadDict = false;
		boolean didInitMgrs = false;
		
		int action = StartupManager.getAction(existsNecessaryFiles, didSetupRun, didTryToLoadDict, didLoadDict, false);
		
		assertEquals(StartupManager.ACTION_DO_SETUP_ACTIVITY, action);
		
		existsNecessaryFiles = false;
		didSetupRun = true;
		
		assertEquals(StartupManager.ACTION_FINISH_USER_DELAYED_OR_CANCELLED_SETUP, StartupManager.getAction(existsNecessaryFiles, didSetupRun, didTryToLoadDict, didLoadDict, false));
		
		existsNecessaryFiles = true;
		didSetupRun = false;
		
		assertEquals(StartupManager.ACTION_DO_LOAD_DICT_SERVICE , StartupManager.getAction(existsNecessaryFiles, didSetupRun, didTryToLoadDict, didLoadDict, false));
		
		existsNecessaryFiles = true;
		didSetupRun = true;
		
		assertEquals(StartupManager.ACTION_DO_LOAD_DICT_SERVICE , StartupManager.getAction(existsNecessaryFiles, didSetupRun, didTryToLoadDict, didLoadDict, false));
		
		didLoadDict = true;
		
		action = StartupManager.getAction(existsNecessaryFiles, didSetupRun, didTryToLoadDict, didLoadDict, false);
		
		assertEquals(StartupManager.ACTION_DO_INIT_MANAGERS, action);
		
		didInitMgrs = true;
		
		action = StartupManager.getAction(existsNecessaryFiles, didSetupRun, didTryToLoadDict, didLoadDict, didInitMgrs);
		
		assertEquals(StartupManager.ACTION_DO_RDICT_ACTIVITY, action);
	}
	
	public void testGetActionForCaseWhenUserPressesBackButtonDuringLoading() {
		boolean existsNecessaryFiles = false;
		boolean didSetupRun = false;
		boolean didTryToLoadDict = false;
		boolean didLoadDict = false;
		boolean didInitMgrs = false;
		
		existsNecessaryFiles = true;
		didTryToLoadDict = true;
		
		assertEquals(StartupManager.ACTION_FINISH_USER_PRESSED_BACK_BUTTON_DURING_DICT_LOAD,
					 StartupManager.getAction(existsNecessaryFiles, didSetupRun, didTryToLoadDict, didLoadDict, didInitMgrs));
	}
}
