package com.amplio.rdict.setup;

import android.content.Context;
import android.view.View;

public abstract class SetupViewWrapper {

	View view = null;
	SetupActivity setupActivity = null;
	Context context = null;
	
	public SetupViewWrapper(Context context, SetupActivity setupActivity) {
		this.context = context;
		this.setupActivity = setupActivity;
	}
	
	public View getView() {
		return this.view;
	}
	
}
