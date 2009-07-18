package com.amplio.rdict.search;




import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DictionaryWebViewClient extends WebViewClient{

	public DictionaryActivity dicActivity = null;
	
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.contains("lookup")) {
			String word = url.substring(url.indexOf('=') + 1);
			this.dicActivity.refreshDicPage(word, true);
			return true;
		}
		else if (url.contains("save")) {
			System.out.println(url);
			String def = url.substring(url.indexOf('=') + 1);
			
			this.dicActivity.addCard(def);
			return true;
		}
		else {
			return false;
		}

	}
}
