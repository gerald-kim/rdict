package com.amplio.rdict.more;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.amplio.rdict.R;
import com.amplio.rdict.SplashActivity;
import com.amplio.rdict.search.DictionaryEntryFactory;

public class AboutActivity extends Activity{
	private static final String FILE_PATH_ABOUT = "rdict_about.html";
	
	private WebView m_aboutWebview = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.about);
		
		m_aboutWebview = (WebView) findViewById(R.id.about_webview);
		m_aboutWebview.getSettings().setJavaScriptEnabled(false);
	}
	
	public void onResume() {
		super.onResume();
		
		InputStream in = SplashActivity.getAssetInputStream(this, FILE_PATH_ABOUT);
		String aboutContents = DictionaryEntryFactory.loadHTMLFileContents(in);
		m_aboutWebview.loadDataWithBaseURL("fake://dagnabbit", aboutContents, "text/html", "utf-8", null);
	}

}
