package com.amplio.rdict.more;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.amplio.rdict.R;
import com.amplio.rdict.SplashActivity;
import com.amplio.rdict.search.DictionaryEntryFactory;

public class HelpActivity extends Activity{
	private static final String FILE_PATH_HELP = "rdict_help.html";
	
	private WebView helpWebview = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.help);
		
		helpWebview = (WebView) findViewById(R.id.help_webview);
		helpWebview.getSettings().setJavaScriptEnabled(false);
	}
	
	public void onResume() {
		super.onResume();
		
		InputStream in = SplashActivity.getAssetInputStream(this, FILE_PATH_HELP);
		String helpContents = DictionaryEntryFactory.loadHTMLFileContents(in);
		helpWebview.loadDataWithBaseURL("fake://dagnabbit", helpContents, "text/html", "utf-8", null);
	}

}
