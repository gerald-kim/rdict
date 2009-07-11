package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;

public class DictionaryActivity extends Activity implements AssetInputStreamProvider {
	private EditText searchText;
	private Button searchButton;
   
	private WebView _searchResultsPage;

    private Dictionary _dictionary = null;
    
    private CardSetManager _cardSetMgr = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);		
		setContentView(R.layout.dictionary);
		
		this.searchText = (EditText)findViewById(R.id.widget43);
		
		//this.searchButton = (Button)findViewById(R.id.widget44);
		//this.searchButton.setOnClickListener(this);
		
		_searchResultsPage = (WebView) findViewById(R.id.webview);
		_searchResultsPage.getSettings().setJavaScriptEnabled(true);
		_searchResultsPage.setWebViewClient(new DictionaryWebViewClient());
		_searchResultsPage.loadData("Search", "text/html", "utf-8");
		
		SQLiteDatabase con = SQLiteDatabase.openDatabase("/sdcard/rdict/word.db", null, SQLiteDatabase.OPEN_READONLY);
    	_dictionary = new Dictionary(con, getAssetInputStream("dictionary_js.html"));
    }
    
    public void onStart(){
    	super.onStart();
    	
    	System.out.println("Dic started ");
    }
    
    public void onResume(){
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
    		
    	if(SearchActivity.searchWord != null){
    		DictionaryEntry dicEntry = this._dictionary.searchByWord(SearchActivity.searchWord);
    		_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit", dicEntry.entry, "text/html", "utf-8", null);
		}
		else {
			_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
		}
    	
		_cardSetMgr = new CardSetManager(RDictActivity.db);
    }
    
    public void onPause() {
    	System.out.println("Dic paused;");
    	super.onPause();
    	
    	super.onPause();
    }
    
    public void onStop(){
    	System.out.println("Dic stopped;");
    	
    	super.onStop();
    }
    
	public InputStream getAssetInputStream(String path) {
		InputStream stream = null;
		try {
			stream = this.getAssets().open(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stream;
	}
	
    private class DictionaryWebViewClient extends WebViewClient{
    	@Override 
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		if (url.contains("lookup")) {
    			String word = url.substring(url.indexOf('=') + 1);
    			DictionaryEntry dicEntry = _dictionary.searchByWord(word);
	
    			if(dicEntry != null){
    				_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit", dicEntry.entry, "text/html", "utf-8", null);
    			}
    			else {
					  _searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
    			}
    			return true;
    		}
    		else if (url.contains("save")) {
    			System.out.println(url);
    			String def = url.substring(url.indexOf('=') + 1);
    			Card card = new Card(searchText.getText().toString(), def);
    			card.schedule();
    			_cardSetMgr.save(card);
    			return true;
    		}
    		else {
    			return false;
    		}

    	}
    }
}
