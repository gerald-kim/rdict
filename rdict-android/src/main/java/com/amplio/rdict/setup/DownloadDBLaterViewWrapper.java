package com.amplio.rdict.setup;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.rdict.R;

public class DownloadDBLaterViewWrapper extends SetupViewWrapper implements OnClickListener {
	Button okButton = null;
	
	public DownloadDBLaterViewWrapper(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		
		this.v = View.inflate( context, R.layout.setup_delayed, null);
		
		this.okButton = (Button) this.v.findViewById(R.id.setup_delayed_button );
		this.okButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		this.okButton.setPressed(false);
		SetupActivity.setupMgr.userAcknowledgedNeedToDownloadLater();
		this.setupActivity.updateLayout();
	}
}
