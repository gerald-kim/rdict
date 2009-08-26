package com.amplio.rdict.search;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.history.History;

public class DictionaryActivity extends Activity implements OnClickListener {
	
	public final static String NEW_FILE_PATH = "/sdcard/rdict/.cardAddedOnce";
	
	public static History sessionHistory = new History();
	
	private TextView title = null;
	private Button backButton = null;
	private Button forwardButton = null;
	
	private ViewFlipper flipper = null;
	private TextView loadingTitle = null;
	private LinearLayout progressBarLayout = null;
	
	private WebView dicPageWebView = null;

	public DictionaryEntry dicEntry = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Bundle extras = getIntent().getExtras();
    	this.dicEntry = new DictionaryEntry(extras.getString(SearchActivity.INTENT_PARAM_HEADWORD),
    										extras.getString(SearchActivity.INTENT_PARAM_CONTENTS));
    	
		setContentView(R.layout.dictionary);

		title = (TextView)findViewById(R.id.title);
		loadingTitle = (TextView)findViewById(R.id.loading_title);
		
		progressBarLayout = (LinearLayout) findViewById(R.id.progressbar_layout);

		flipper = (ViewFlipper)findViewById(R.id.webview_flipper);
		
		backButton = (Button)findViewById(R.id.back_button);
		backButton.setOnClickListener(this);
		
		forwardButton = (Button)findViewById(R.id.forward_button);
		forwardButton.setOnClickListener(this);
		
		dicPageWebView = (WebView)findViewById(R.id.webview);
		dicPageWebView.getSettings().setJavaScriptEnabled(true);
		
		DictionaryWebViewClient client = new DictionaryWebViewClient();
		client.dicActivity = this;
		client.context = this.getApplicationContext();
		dicPageWebView.setWebViewClient(client);
    }
        
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	RDictActivity.odb.commit();
    }


    @Override
	public void onResume(){
    	super.onResume();
    	
    	this.refreshDicPage();
    }
    
    public void refreshDicPage() {
    	if(this.dicEntry != null) {
    		loadingTitle.setText(dicEntry.headword);
    		title.setText(dicEntry.headword);
    		
    		StringBuilder sb = new StringBuilder(dicEntry.contents);
    		sb.append(getCCBySALicenseString(dicEntry.headword));
    		
    		dicPageWebView.loadDataWithBaseURL("fake://dagnabbit", sb.toString(), "text/html", "utf-8", null);
    	}
		else {
			this.title.setText("No Results");
			dicPageWebView.loadDataWithBaseURL("fake://dagnabbit","Sorry, no results.", "text/html", "utf-8", null);
		}
    	
    	this.backButton.setEnabled(DictionaryActivity.sessionHistory.canGoBack());
    	this.forwardButton.setEnabled(DictionaryActivity.sessionHistory.canGoForward());	
    }
    
    public void showLoadingTitle() {
	    if(! isShowingLoadingTitle())
	    	flipper.showPrevious();  
    }
    
    public void showDictionaryTitle() {
    	if(isShowingLoadingTitle())
    		flipper.showNext();
    }

	private boolean isShowingLoadingTitle() {
		return progressBarLayout ==  flipper.getCurrentView();
    }
    
    private String getCCBySALicenseString( String headword ) {
	    return "<br>View the original Wiktionary page <a href=\"http://en.wiktionary.org/wiki/" + headword + "\">here</a>.";
    }

	public void addCard(String def){
		RDictActivity.cardSetManager.create( title.getText().toString(), def );
    }
    
    public void onClick(View v) {
		if(this.backButton == v)
			DictionaryActivity.sessionHistory.goBack();
		else
			DictionaryActivity.sessionHistory.goForward();
		
		this.dicEntry = RDictActivity.dictionary.searchByWord(DictionaryActivity.sessionHistory.getWord().headword);
		this.refreshDicPage();
	}

	public boolean isFirstTime() {
		return ! new File(NEW_FILE_PATH).exists();
    }

	public void writeFirstTimeFile() {
		try {
	        new File(NEW_FILE_PATH).createNewFile();
        } catch( IOException e ) {
	        e.printStackTrace();
        }
    }
}
