package com.amplio.vkbl8r.setup;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public abstract class SetupView extends View implements OnClickListener {

	SetupActivity sa = null;
	SetupManager setupMgr = null;
	
	public abstract void addToLayout(LinearLayout l);
	
	public SetupView(Context context) {
		super(context);
	}
	
	public void setSetupActivity(SetupActivity sa) {
		this.sa = sa;
	}
	public void setSetupManager(SetupManager setupMgr) {
		this.setupMgr = setupMgr;
	}
	
}
