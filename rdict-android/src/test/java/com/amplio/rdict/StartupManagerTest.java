package com.amplio.rdict;

import junit.framework.TestCase;

public class StartupManagerTest extends TestCase {
	
	public void testGetAction() {
		boolean existsNecessaryFiles = false;
		boolean didSetupRun = false;
		boolean didLoadDict = false;
		boolean didInitMgrs = false;
		
		int action = StartupManager.getAction(existsNecessaryFiles, didSetupRun, didLoadDict, false);
		
		assertEquals(StartupManager.ACTION_DO_SETUP_ACTIVITY, action);
		
		existsNecessaryFiles = false;
		didSetupRun = true;
		
		assertEquals(StartupManager.ACTION_FINISH_USER_DELAYED_SETUP, StartupManager.getAction(existsNecessaryFiles, didSetupRun, didLoadDict, false));
		
		existsNecessaryFiles = true;
		didSetupRun = false;
		
		assertEquals(StartupManager.ACTION_DO_LOAD_DICT_SERVICE , StartupManager.getAction(existsNecessaryFiles, didSetupRun, didLoadDict, false));
		
		existsNecessaryFiles = true;
		didSetupRun = true;
		
		assertEquals(StartupManager.ACTION_DO_LOAD_DICT_SERVICE , StartupManager.getAction(existsNecessaryFiles, didSetupRun, didLoadDict, false));
		
		didLoadDict = true;
		
		action = StartupManager.getAction(existsNecessaryFiles, didSetupRun, didLoadDict, false);
		
		assertEquals(StartupManager.ACTION_DO_INIT_MANAGERS, action);
		
		didInitMgrs = true;
		
		action = StartupManager.getAction(existsNecessaryFiles, didSetupRun, didLoadDict, didInitMgrs);
		
		assertEquals(StartupManager.ACTION_DO_RDICT_ACTIVITY, action);
	}
}
