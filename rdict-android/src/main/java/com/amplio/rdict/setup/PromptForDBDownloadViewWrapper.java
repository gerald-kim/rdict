package com.amplio.rdict.setup;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.rdict.R;

public class PromptForDBDownloadViewWrapper extends SetupViewWrapper implements OnClickListener {
	Button laterButton = null;
	Button nowButton = null;
	
	public PromptForDBDownloadViewWrapper(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		
		this.v = View.inflate(context, R.layout.setup_prompt_for_db_download, null);
		
		this.laterButton = (Button) this.v.findViewById(R.id.download_later_button );
		this.laterButton.setOnClickListener(this);
		
		this.nowButton = (Button) this.v.findViewById(R.id.download_now_button );
		this.nowButton.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		if(v == this.nowButton)
			SetupActivity.setupMgr.userChoseDownloadOption();
		else
			SetupActivity.setupMgr.userChoseToDelayDownlaod();
		
		this.setupActivity.updateLayout();
	}
}
