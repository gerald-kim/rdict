package com.amplio.rdict.search;

import java.net.URLDecoder;

import android.app.AlertDialog;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.amplio.rdict.RDictActivity;

public class DictionaryWebViewClient extends WebViewClient{

	private static final CharSequence HELP_MESG = "When you pressed the Star Button, "
												+ "RDict created a new flashcard for you and "
												+ "added it to your collection.\n\n" 
												+ "When it's time to practice the card, " 
												+ "RDict will notify you using the Review tab.  " 
												+ "See More > Help for more information.\n\n"
												+ "To practice the card now, " 
												+ "select the Review tab and try an Early Practice quiz.";
	
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
			if(! dicActivity.isFirstTime()) {
				Toast.makeText(this.context, "New Card Added.", Toast.LENGTH_SHORT).show();
			}
			else {
				new AlertDialog.Builder(this.dicActivity)
				.setTitle("Info")
				.setMessage(HELP_MESG)
				.setNeutralButton("Ok", null)
				.show();
				
				dicActivity.writeFirstTimeFile();
			}
			
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
