package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;

public class DictionaryActivity extends Activity {
	private EditText searchText;
	private Button searchButton;
    private static WebView searchResultsPage;

    private Dictionary _dictionary = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);		
		setContentView(R.layout.dictionary);
		
		this.searchText = (EditText)findViewById(R.id.widget43);
		
		this.searchButton = (Button)findViewById(R.id.widget44);
		this.searchButton.setOnClickListener(mCorkyListener);

		DictionaryActivity.searchResultsPage = (WebView) findViewById(R.id.webview);
		DictionaryActivity.searchResultsPage.getSettings().setJavaScriptEnabled(true);
		DictionaryActivity.searchResultsPage.loadData("Search", "text/html", "utf-8");
		
		InputStream htmlStream = this.loadAssetAsStream("dictionary_js.html");
		
		_dictionary = loadDictionary(htmlStream);
    }
    
    private OnClickListener mCorkyListener = new OnClickListener() {
        public void onClick(View v) {
           	DictionaryEntry dicEntry = _dictionary.searchByWord(searchText.getText().toString());
	  		
	      	if(dicEntry != null){
	      		DictionaryActivity.searchResultsPage.loadDataWithBaseURL("fake://dagnabbit",dicEntry.entry, "text/html", "utf-8", null);
	      	}
	      	else{
	      		DictionaryActivity.searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
	      	}
        }
    };

	private Dictionary loadDictionary(InputStream htmlStream) {
		String[] assetPaths = this.getAssetPaths();
      	
		Vector<InputStream> _assetInputstreams = new Vector<InputStream>();
		
  		for(int i = 0; i < assetPaths.length;i++) {
  			if(-1 != assetPaths[i].indexOf("word"))
  				_assetInputstreams.add(this.loadAssetAsStream(assetPaths[i]));
      	}
  		
  		return new Dictionary(htmlStream, _assetInputstreams);
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
    
    private InputStream loadAssetAsStream(String path){
    	InputStream stream = null;
		try {
			stream = this.getAssets().open(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stream;
    }
}
