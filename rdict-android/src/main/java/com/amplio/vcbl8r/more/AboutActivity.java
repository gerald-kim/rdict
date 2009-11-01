package com.amplio.vcbl8r.more;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.amplio.vcbl8r.R;
import com.amplio.vcbl8r.SplashActivity;
import com.amplio.vcbl8r.search.DictionaryEntryFactory;

public class AboutActivity extends Activity{
	private static final String FILE_PATH_ABOUT = "rdict_about.html";
	
	private WebView aboutWebview = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.about);
		
		aboutWebview = (WebView) findViewById(R.id.about_webview);
		aboutWebview.getSettings().setJavaScriptEnabled(false);
	}
	
	public void onResume() {
		super.onResume();
		
		InputStream in = SplashActivity.getAssetInputStream(this, FILE_PATH_ABOUT);
		String aboutContents = DictionaryEntryFactory.loadHTMLFileContents(in);
		aboutWebview.loadDataWithBaseURL("fake://dagnabbit", aboutContents, "text/html", "utf-8", null);
	}

}
