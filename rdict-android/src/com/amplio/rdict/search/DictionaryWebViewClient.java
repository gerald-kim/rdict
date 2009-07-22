package com.amplio.rdict.search;




import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.review.StatisticsManager;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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
			
			new StatisticsManager(RDictActivity.db).saveOrUpdateCardStackStatistics();
			
			return true;
		}
		else {
			return false;
		}

	}
}
