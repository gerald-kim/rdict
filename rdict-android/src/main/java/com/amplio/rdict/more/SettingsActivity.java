package com.amplio.rdict.more;

import android.app.Activity;
import android.os.Bundle;

import com.amplio.rdict.R;
import com.amplio.rdict.setup.DBDownloadingView;

public class SettingsActivity extends Activity{
	
	DBDownloadingView downloadView = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.settings);	
	}
}
