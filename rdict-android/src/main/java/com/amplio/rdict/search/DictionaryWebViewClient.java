package com.amplio.rdict.search;




import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.amplio.rdict.RDictActivity;

public class DictionaryWebViewClient extends WebViewClient{

	public DictionaryActivity dicActivity = null;
	public Context context = null;
	
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.contains("lookup")) {
			String word = url.substring(url.indexOf('=') + 1);
			this.dicActivity.refreshDicPage(word, true);
			return true;
		}
		else if (url.contains("save")) {
			Toast.makeText(this.context, "Added.", Toast.LENGTH_SHORT).show();
			String def = url.substring(url.indexOf('=') + 1);
			this.dicActivity.addCard(def);
			
			RDictActivity.c_statisticsManager.saveOrUpdateCardStackStatistics();
			
			return true;
		}
		else {
			return false;
		}

	}
}