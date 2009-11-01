package com.amplio.vcbl8r.setup;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeView extends SetupView implements OnClickListener {

	TextView mesg = null;
	Button okButton = null;
	
	public WelcomeView(Context context) {
		super(context);
		
		this.mesg = new TextView(context);
		this.mesg.setText("Thank you for downloading Vocabulator.");
		
		this.okButton = new Button(context);
		this.okButton.setText("Ok");
		this.okButton.setOnClickListener(this);
	}
	
	public void addToLayout(LinearLayout l) {
		l.addView(this.mesg);
		l.addView(this.okButton);
	}
	
	public void onClick(View v) {
		this.setupMgr.userAcknowledgedWelcomeScreen();
		this.sa.updateLayout();
	}
}
