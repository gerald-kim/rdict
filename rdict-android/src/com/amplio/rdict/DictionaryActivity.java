package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;

public class DictionaryActivity extends Activity implements AssetInputStreamProvider, OnClickListener{
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
		this.searchButton = (Button)findViewById(R.id.widget44);
		this.searchButton.setOnClickListener(this);
		
		_searchResultsPage = (WebView) findViewById(R.id.webview);
		_searchResultsPage.getSettings().setJavaScriptEnabled(true);
		_searchResultsPage.setWebViewClient(new DictionaryWebViewClient());
		_searchResultsPage.loadData("Search", "text/html", "utf-8");
		
		_dictionary = new Dictionary(getAssetInputStream("dictionary_js.html"), this.getAssetPaths(), this);
    }
    
    public void onStart(){
    	super.onStart();
    	
    	System.out.println("Dic started ");
    }
    
    public void onResume(){
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
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
    
	private String[] getAssetPaths(){
		String [] assetPaths = null;
      	try {
  			 assetPaths = this.getAssets().list("");
  		} catch (IOException e1) {
  			e1.printStackTrace();
  		}
  		
  		return assetPaths;
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

	public void onClick(View v) {
		DictionaryEntry dicEntry = _dictionary.searchByWord(searchText.getText().toString());
    	
		if(dicEntry != null){
			_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit", dicEntry.entry, "text/html", "utf-8", null);
		}
		else {
			_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
		}
	}
}
