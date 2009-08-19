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

public class DictionaryActivity extends Activity implements OnClickListener {
	public static History sessionHistory = new History();
	
	private TextView m_title = null;
	private Button m_backButton = null;
	private Button m_forwardButton = null;
	private WebView m_searchResultsPage = null;

	public DictionaryEntry m_dicEntry = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Bundle extras = getIntent().getExtras();
    	    	
    	this.m_dicEntry = new DictionaryEntry(extras.getString(SearchActivity.INTENT_PARAM_HEADWORD),
    										extras.getString(SearchActivity.INTENT_PARAM_CONTENTS));
    	
		setContentView(R.layout.dictionary);
    	
		this.m_title = (TextView)findViewById(R.id.title);
		
		m_backButton = (Button)findViewById(R.id.back_button);
		m_backButton.setOnClickListener(this);
		
		m_forwardButton = (Button)findViewById(R.id.forward_button);
		m_forwardButton.setOnClickListener(this);
		
		m_searchResultsPage = (WebView) findViewById(R.id.webview);
		m_searchResultsPage.getSettings().setJavaScriptEnabled(true);
		
		DictionaryWebViewClient client = new DictionaryWebViewClient();
		client.dicActivity = this;
		client.context = this.getApplicationContext();
		m_searchResultsPage.setWebViewClient(client);
    }
        
    public void onResume(){
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
    	
    	this.refreshDicPage();
    }
    
    public void refreshDicPage() {
    	if(this.m_dicEntry != null) {
    		this.m_title.setText(this.m_dicEntry.headword);
    		
    		StringBuilder sb = new StringBuilder(this.m_dicEntry.contents);
    		
    		sb.append(getCCBySALicenseString(m_dicEntry.headword));
    		
    		m_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit", sb.toString(), "text/html", "utf-8", null);
    	}
		else {
			this.m_title.setText("No Results");
			m_searchResultsPage.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
		}
    	
    	this.m_backButton.setEnabled(DictionaryActivity.sessionHistory.canGoBack());
    	this.m_forwardButton.setEnabled(DictionaryActivity.sessionHistory.canGoForward());	
    }
    
    private String getCCBySALicenseString( String headword ) {
	    return "<br>View the original Wiktionary page <a href=\"http://en.wiktionary.org/wiki/" + headword + "\">here</a>.";
    }

	public void addCard(String def){
		RDictActivity.c_cardSetManager.create( m_title.getText().toString(), def );
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
		if(this.m_backButton == v)
			DictionaryActivity.sessionHistory.goBack();
		else
			DictionaryActivity.sessionHistory.goForward();
		
		this.m_dicEntry = RDictActivity.c_dictionary.searchByWord(DictionaryActivity.sessionHistory.getWord().headword);
		this.refreshDicPage();
	}
}
