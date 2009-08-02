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
	public static DictionaryEntry dicEntry = null;
	public static History sessionHistory = new History();
	
	private TextView title = null;
	private Button _backButton = null;
	private Button _forwardButton = null;
	private WebView _searchResultsPage = null;
	
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
    }
        
    public void onResume(){
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
    	
    	this.refreshDicPage();
    }
    
    public void refreshDicPage() {
    	if(DictionaryActivity.dicEntry != null) {
    		this.title.setText(DictionaryActivity.dicEntry.headword);
    		_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit", DictionaryActivity.dicEntry.contents, "text/html", "utf-8", null);
    	}
		else {
			this.title.setText("No Results");
			_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
		}
    	
    	this._backButton.setEnabled(DictionaryActivity.sessionHistory.canGoBack());
    	this._forwardButton.setEnabled(DictionaryActivity.sessionHistory.canGoForward());	
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
			DictionaryActivity.sessionHistory.goBack();
		else
			DictionaryActivity.sessionHistory.goForward();
		
		DictionaryActivity.dicEntry = RDictActivity.c_dictionary.searchByWord(DictionaryActivity.sessionHistory.getWord().headword);
		this.refreshDicPage();
	}
}
