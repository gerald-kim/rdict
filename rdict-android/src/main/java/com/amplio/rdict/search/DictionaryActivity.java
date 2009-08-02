package com.amplio.rdict.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.history.History;
import com.amplio.rdict.review.Card;

public class DictionaryActivity extends Activity implements OnClickListener {
	private TextView title = null;
	private Button _backButton = null;
	private Button _forwardButton = null;
	private WebView _searchResultsPage = null;

	History searchHistory = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary);
    	
		this.title = (TextView)findViewById(R.id.title);
		
		_backButton = (Button)findViewById(R.id.back_button);
		_backButton.setOnClickListener(this);
		
		_forwardButton = (Button)findViewById(R.id.forward_button);
		_forwardButton.setOnClickListener(this);
		
		_searchResultsPage = (WebView) findViewById(R.id.webview);
		_searchResultsPage.getSettings().setJavaScriptEnabled(true);
		
		DictionaryWebViewClient client = new DictionaryWebViewClient();
		client.dicActivity = this;
		client.context = this.getApplicationContext();
		_searchResultsPage.setWebViewClient(client);
		
		this.searchHistory = new History();
    }
    
    public void onStart(){
    	super.onStart();
    	
    	System.out.println("Dic started ");
    }
    
    public void onResume(){
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
    	
    	this.refreshDicPage(SearchActivity.searchWord.headword, true);
    }
    
    public void refreshDicPage(String word, boolean recordHistory) {
    	DictionaryEntry dicEntry = RDictActivity.c_dictionary.searchByWord(word);
    	this.title.setText(word);
    	
    	if(recordHistory)
			this.searchHistory.addWord(dicEntry);
    	
    	if(dicEntry != null)
    		_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit", dicEntry.contents, "text/html", "utf-8", null);
		else
			_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
    	
    	this._backButton.setEnabled(this.searchHistory.canGoBack());
    	this._forwardButton.setEnabled(this.searchHistory.canGoForward());	
    }
    
    public void addCard(String def){
    	Card card = new Card(title.getText().toString(), def);
		card.schedule();
		RDictActivity.c_cardSetManager.save(card);
    }
    
    public void onPause() {
    	System.out.println("Dic paused;");    	
    	super.onPause();
    }
    
    public void onStop(){
    	System.out.println("Dic stopped;");
    	
    	super.onStop();
    }

    public void onClick(View v) {
		if(this._backButton == v)
			this.searchHistory.goBack();
		else
			this.searchHistory.goForward();
		
		this.refreshDicPage(this.searchHistory.getWord().headword, false);
	}

}
