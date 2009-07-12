package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;

public class DictionaryActivity extends Activity implements AssetInputStreamProvider, OnClickListener {
	private TextView title = null;
	private Button _backButton = null;
	private Button _forwardButton = null;
	private WebView _searchResultsPage = null;

    private Dictionary _dictionary = null;
    private CardSetManager _cardSetMgr = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary);
    	
    	SQLiteDatabase con = SQLiteDatabase.openDatabase("/sdcard/rdict/word.db", null, SQLiteDatabase.OPEN_READONLY);
    	_dictionary = new Dictionary(con, getAssetInputStream("dictionary_js.html"));
    	
    	_cardSetMgr = new CardSetManager(RDictActivity.db);
    	
		this.title = (TextView)findViewById(R.id.title);
		
		_backButton = (Button)findViewById(R.id.back_button);
		_backButton.setOnClickListener(this);
		
		_forwardButton = (Button)findViewById(R.id.forward_button);
		_forwardButton.setOnClickListener(this);
		
		_searchResultsPage = (WebView) findViewById(R.id.webview);
		_searchResultsPage.getSettings().setJavaScriptEnabled(true);
		
		DictionaryWebViewClient client = new DictionaryWebViewClient();
		client.dicActivity = this;
		_searchResultsPage.setWebViewClient(client);
    }
    
    public void onStart(){
    	super.onStart();
    	
    	System.out.println("Dic started ");
    }
    
    public void onResume(){
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
    	
    	this.refreshDicPage(SearchActivity.searchWord._word, true);
    	    	
		_cardSetMgr = new CardSetManager(RDictActivity.db);
    }
    
    public void refreshDicPage(String word, boolean recordHistory) {
    	DictionaryEntry dicEntry = _dictionary.searchByWord(word);
    	this.title.setText(word);
    	
    	if(recordHistory)
			SearchActivity.searchHistory.addWord(new Word(1, word, null));
    	
    	if(dicEntry != null)
    		_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit", dicEntry.entry, "text/html", "utf-8", null);
		else
			_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
    	
    	this._backButton.setEnabled(SearchActivity.searchHistory.canGoBack());
    	this._forwardButton.setEnabled(SearchActivity.searchHistory.canGoForward());	
    }
    
    public void addCard(String def){
    	Card card = new Card(title.getText().toString(), def);
		card.schedule();
		_cardSetMgr.save(card);
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

	public void onClick(View v) {
		if(this._backButton == v)
			SearchActivity.searchHistory.goBack();
		else
			SearchActivity.searchHistory.goForward();
		
		this.refreshDicPage(SearchActivity.searchHistory.getWord()._word, false);
	}
}
