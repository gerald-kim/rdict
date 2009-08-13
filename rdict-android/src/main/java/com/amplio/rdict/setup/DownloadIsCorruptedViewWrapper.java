package com.amplio.rdict.setup;

import com.amplio.rdict.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DownloadIsCorruptedViewWrapper extends SetupViewWrapper implements OnClickListener {

	Button laterButton = null;
	Button nowButton = null;
	
	public DownloadIsCorruptedViewWrapper( Context context, SetupActivity setupActivity ) {
	    super( context, setupActivity );

	    this.v = View.inflate(context, R.layout.setup_download_is_corrupted, null);
		
		this.laterButton = (Button) this.v.findViewById(R.id.try_again_later_button );
		this.laterButton.setOnClickListener(this);
		
		this.nowButton = (Button) this.v.findViewById(R.id.try_again_now_button );
		this.nowButton.setOnClickListener(this);
    }

	public void onClick( View v ) {
		if(v == this.nowButton) {
			SetupActivity.setupMgr.userChoseDownloadOption();
			setupActivity.downloadingDBViewWrapper = new DBDownloadingViewWrapper(context, setupActivity);
		}
		else {
			SetupActivity.setupMgr.userChoseToDelayDownlaod();
		}
		
		this.setupActivity.updateLayout();	    
    }

}
