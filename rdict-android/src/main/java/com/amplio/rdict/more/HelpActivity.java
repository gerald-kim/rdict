package com.amplio.rdict.more;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.search.DictionaryEntryFactory;

public class HelpActivity extends Activity{
	private static final String FILE_PATH_HELP = "rdict_help.html";
	
	private WebView m_helpWebview = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.help);
		
		m_helpWebview = (WebView) findViewById(R.id.help_webview);
		m_helpWebview.getSettings().setJavaScriptEnabled(false);
	}
	
	public void onResume() {
		super.onResume();
		
		InputStream in = RDictActivity.getAssetInputStream(this, FILE_PATH_HELP);
		String helpContents = DictionaryEntryFactory.loadHTMLFileContents(in);
		m_helpWebview.loadDataWithBaseURL("fake://dagnabbit", helpContents, "text/html", "utf-8", null);
	}

}