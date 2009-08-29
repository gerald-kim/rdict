package com.amplio.rdict.setup;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.rdict.R;

public class DBDownloadFinishedViewWrapper extends SetupViewWrapper implements OnClickListener {
	Button finishedButton = null;
	
	public DBDownloadFinishedViewWrapper(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		
		this.v = View.inflate( context, R.layout.setup_finished, null);
		
		this.finishedButton = (Button) this.v.findViewById(R.id.setup_finished_button);
		this.finishedButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		this.finishedButton.setPressed(false);
		SetupActivity.setupMgr.userClickedFinish();
		this.setupActivity.updateLayout();
	}

}
