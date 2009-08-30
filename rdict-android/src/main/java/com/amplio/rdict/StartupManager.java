package com.amplio.rdict;

public class StartupManager {

	public static final int ACTION_DO_SETUP_ACTIVITY = 1;
	public static final int ACTION_DO_LOAD_DICT_SERVICE = 2;
	public static final int ACTION_DO_INIT_MANAGERS = 3;
	public static final int ACTION_DO_RDICT_ACTIVITY = 4;
	public static final int ACTION_FINISH_USER_DELAYED_OR_CANCELLED_SETUP = 8;
	public static final int ACTION_FINISH_USER_PRESSED_BACK_BUTTON = 9;
	
	public static int getAction(boolean existsNecessaryFiles, boolean didSetupRun, boolean didTryToLoadDict, boolean didLoadDict, boolean didInitMgrs) {
	    if(existsNecessaryFiles) {
	    	if(! didLoadDict){
	    		if(! didTryToLoadDict)
	    			return ACTION_DO_LOAD_DICT_SERVICE;
	    		else
	    			return ACTION_FINISH_USER_PRESSED_BACK_BUTTON;
	    	}
	    	else if (! didInitMgrs)
	    		return ACTION_DO_INIT_MANAGERS;
	    	else
	    		return ACTION_DO_RDICT_ACTIVITY;
	    }
	    else {
	    	if(! didSetupRun)
	    		return ACTION_DO_SETUP_ACTIVITY;
	    	else
	    		return ACTION_FINISH_USER_DELAYED_OR_CANCELLED_SETUP;
	    }
    }
}
