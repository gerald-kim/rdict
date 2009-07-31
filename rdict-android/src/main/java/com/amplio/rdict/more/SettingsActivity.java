package com.amplio.rdict.more;

import android.app.Activity;
import android.os.Bundle;

import com.amplio.rdict.R;
import com.amplio.rdict.setup.DBDownloadingViewWrapper;

public class SettingsActivity extends Activity{
	
	DBDownloadingViewWrapper downloadView = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.settings);	
	}
}
