package com.amplio.vcbl8r.setup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.vcbl8r.R;

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
			new AlertDialog.Builder(this.setupActivity)
        	.setTitle("Notice")
        	.setMessage("Vocabulator has detected that your phone is not currently connected to the internet and will now exit.  Please restart Vocabulator when you have an internet connection.")
        	.setNeutralButton("Ok", new DialogInterface.OnClickListener(){
				public void onClick( DialogInterface dialog, int which ) {
					setupActivity.finish();	                
                }})
        	.show();
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
