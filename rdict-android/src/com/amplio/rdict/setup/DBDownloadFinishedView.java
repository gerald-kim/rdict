package com.amplio.vkbl8r.setup;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DBDownloadFinishedView extends SetupView {

	TextView finishedMesg = null;
	Button finishedButton = null;
	
	public DBDownloadFinishedView(Context context) {
		super(context);
		
		this.finishedMesg = new TextView(context);
		this.finishedMesg.setText("The dictionary has been downloaded and installed.");
		
		this.finishedButton = new Button(context);
		this.finishedButton.setText("Use Vocabulator");
		this.finishedButton.setOnClickListener(this);
	}

	@Override
	public void addToLayout(LinearLayout l) {
		l.addView(finishedMesg);
		l.addView(finishedButton);
	}

	public void onClick(View v) {
		this.setupMgr.userClickedFinish();
		this.sa.updateLayout();
	}

}
