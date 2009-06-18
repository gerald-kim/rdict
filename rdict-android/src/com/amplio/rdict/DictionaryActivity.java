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

    private Vector _assetInputstreams = new Vector();
    
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
		
		String [] assetPaths = null;
      	try {
  			 assetPaths = this.getAssets().list("");
  		} catch (IOException e1) {
  			e1.printStackTrace();
  		}
      	
  		for(int i = 0; i < assetPaths.length;i++) {
  			if(-1 != assetPaths[i].indexOf("word")){
	      		try {
	      			_assetInputstreams.add(this.getAssets().open(assetPaths[i]));
	  			} catch (IOException e) {
	  				e.printStackTrace();
	  			}
  			}
      	}
  		
  		InputStream htmlStream = null;
		try {
			htmlStream = this.getAssets().open("dictionary_js.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
  		
  		_dictionary = new Dictionary(htmlStream, _assetInputstreams);
    }
    
    private OnClickListener mCorkyListener = new OnClickListener() {
        public void onClick(View v) {
           	DictionaryEntry dicEntry = _dictionary.searchByWord(searchText.getText().toString());
	  		
	      	if(dicEntry != null){
	      		System.out.println(dicEntry.entry);
	      		DictionaryActivity.searchResultsPage.loadDataWithBaseURL("fake://dagnabbit",dicEntry.entry, "text/html", "utf-8", null);
	      	}
	      	else{
	      		DictionaryActivity.searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
	      	}
        }
    };

}
