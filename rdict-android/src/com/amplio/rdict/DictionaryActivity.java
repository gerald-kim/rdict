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

public class DictionaryActivity extends Activity implements AssetInputStreamProvider{
	private EditText searchText;
	private Button searchButton;
    private WebView _searchResultsPage;

    private Dictionary _dictionary = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);		
		setContentView(R.layout.dictionary);
		
		this.searchText = (EditText)findViewById(R.id.widget43);
		this.searchButton = (Button)findViewById(R.id.widget44);
		this.searchButton.setOnClickListener(_searchButtonListener);
		
		_searchResultsPage = (WebView) findViewById(R.id.webview);
		_searchResultsPage.getSettings().setJavaScriptEnabled(true);
		_searchResultsPage.setWebViewClient(new DictionaryWebViewClient());
		_searchResultsPage.loadData("Search", "text/html", "utf-8");
		
		_dictionary = new Dictionary(getAssetInputStream("dictionary_js.html"), this.getAssetPaths(), this);
    }

    private OnClickListener _searchButtonListener = new OnClickListener() {
        public void onClick(View v) {
        	DictionaryEntry dicEntry = _dictionary.searchByWord(searchText.getText().toString());
        	
			if(dicEntry != null){
				_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit", dicEntry.entry, "text/html", "utf-8", null);
			}
			else {
				  _searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
			}
        }
    };
    
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
    			System.out.println(word);
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
    			System.out.println("Card Saved!");
    			return true;
    		}
    		else {
    			return false;
    		}

    	}
    }
}
