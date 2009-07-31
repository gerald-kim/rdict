package com.amplio.rdict.setup;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.rdict.R;

public class WelcomeViewWrapper extends SetupViewWrapper implements OnClickListener {
	public Button okButton = null;
	
	public WelcomeViewWrapper(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		
		this.v = View.inflate( context, R.layout.setup_welcome, null);
		
		this.okButton = (Button) this.v.findViewById(R.id.welcome_view_ok_button );
		this.okButton.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		SetupActivity.setupMgr.userAcknowledgedWelcomeScreen();
		this.setupActivity.updateLayout();
	}
}
