package com.amplio.vcbl8r.more;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.amplio.vcbl8r.R;
import com.amplio.vcbl8r.SplashActivity;
import com.amplio.vcbl8r.search.DictionaryEntryFactory;

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
