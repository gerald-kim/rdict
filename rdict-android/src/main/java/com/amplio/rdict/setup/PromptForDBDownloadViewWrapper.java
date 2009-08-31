package com.amplio.rdict.setup;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.rdict.R;

public class PromptForDBDownloadViewWrapper extends SetupViewWrapper implements OnClickListener {
	Button laterButton = null;
	Button nowButton = null;
	private boolean isConnected;

	public PromptForDBDownloadViewWrapper( Context context, SetupActivity setupActivity, boolean isConnected ) {
		super( context, setupActivity );

		this.isConnected = isConnected;
		this.view = View.inflate( context, R.layout.setup_prompt_for_db_download, null );

		this.laterButton = (Button) this.view.findViewById( R.id.download_later_button );
		this.laterButton.setOnClickListener( this );

		this.nowButton = (Button) this.view.findViewById( R.id.download_now_button );
		this.nowButton.setOnClickListener( this );
	}

	public void onClick( View v ) {
		if( v == this.nowButton && this.isConnected ) {
			nowButtonClicked();
		} else if (v == this.nowButton && !this.isConnected ) {
			//XXX: change activity when no wireless connection
			laterButtonClicked();
		} else {
			laterButtonClicked();
		}

		this.setupActivity.updateLayout();
	}

	private void nowButtonClicked() {
	    this.nowButton.setPressed( false );

	    SetupActivity.setupMgr.userChoseDownloadOption();

	    DownloadService.dm = new DownloadMonitor(
	            this.setupActivity.downloadingDBViewWrapper.downloadingViewHandler,
	            this.setupActivity.downloadingDBViewWrapper.getDownloadRunnable() );

	    this.setupActivity
	            .startService( new Intent( this.setupActivity, DownloadService.class ) );
    }

	private void laterButtonClicked() {
	    this.laterButton.setPressed( false );
	    SetupActivity.setupMgr.userChoseToDelayDownlaod();
    }

}
