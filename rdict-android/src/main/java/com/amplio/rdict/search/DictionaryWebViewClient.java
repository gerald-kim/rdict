package com.amplio.rdict.search;

import java.net.URLDecoder;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.amplio.rdict.RDictActivity;

public class DictionaryWebViewClient extends WebViewClient{

	public DictionaryActivity dicActivity = null;
	public Context context = null;
	private String m_url;
	
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.contains("lookup")) {
			String word = url.substring(url.indexOf('=') + 1);
			
			dicActivity.m_dicEntry = RDictActivity.c_dictionary.searchByWord(word);
			
			DictionaryActivity.sessionHistory.addWord(dicActivity.m_dicEntry);
			
			this.dicActivity.refreshDicPage();
			return true;
		}
		else if (url.contains("save")) {
			Toast.makeText(this.context, "Added.", Toast.LENGTH_SHORT).show();
			
			String def = url.substring(url.indexOf('=') + 1);
			this.dicActivity.addCard(URLDecoder.decode(def));
			
			RDictActivity.c_statisticsManager.saveOrUpdateCardStackStatistics();
			
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void onLoadResource(WebView view, String url) {
		dicActivity.showLoadingTitle();
		
		super.onLoadResource(view, url);
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		dicActivity.showDictionaryTitle();
		
		super.onPageFinished(view, url);
	}
}
