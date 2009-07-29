package com.amplio.rdict.setup;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DownloadDBLaterView extends SetupView {
	TextView mesg = null;
	Button okButton = null;
	
	public DownloadDBLaterView(Context context) {
		super(context);
		
		this.mesg = new TextView(context);
		this.mesg.setText("You have chosen to download the db later.  RDict will ask you to download again next time you start.");
		
		this.okButton = new Button(context);
		this.okButton.setText("Ok");
		this.okButton.setOnClickListener(this);
	}

	@Override
	public void addToLayout(LinearLayout l) {
		l.addView(this.mesg);
		l.addView(this.okButton);
	}

	public void onClick(View v) {
		this.setupMgr.userAcknowledgedNeedToDownloadLater();
		this.sa.updateLayout();
	}
}
