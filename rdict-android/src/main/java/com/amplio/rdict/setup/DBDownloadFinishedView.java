package com.amplio.rdict.setup;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DBDownloadFinishedView extends SetupView implements OnClickListener {

	TextView finishedMesg = null;
	Button finishedButton = null;
	
	public DBDownloadFinishedView(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		
		this.finishedMesg = new TextView(context);
		this.finishedMesg.setText("The dictionary has been downloaded and installed.");
		
		this.finishedButton = new Button(context);
		this.finishedButton.setText("Use RDict");
		this.finishedButton.setOnClickListener(this);
	}

	@Override
	public void addToLayout(LinearLayout l) {
		l.addView(finishedMesg);
		l.addView(finishedButton);
	}

	public void onClick(View v) {
		SetupActivity.setupMgr.userClickedFinish();
		this.setupActivity.updateLayout();
	}

}
