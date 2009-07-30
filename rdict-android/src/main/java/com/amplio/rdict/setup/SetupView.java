package com.amplio.rdict.setup;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public abstract class SetupView extends View {

	SetupActivity setupActivity = null;
	
	public SetupView(Context context, SetupActivity setupActivity) {
		super(context);
		
		this.setupActivity = setupActivity;
	}
	
	public abstract void addToLayout(LinearLayout l);
	
}
