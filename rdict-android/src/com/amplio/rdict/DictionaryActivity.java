package com.amplio.rdict;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.strangegizmo.cdb.ModdedCdb;

public class DictionaryActivity extends Activity {

	private EditText searchText;
	private Button searchButton;
    private static WebView searchResultsPage;
   
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
    }
    
    private OnClickListener mCorkyListener = new OnClickListener() {
        public void onClick(View v) {
        	System.out.println("You searched for " + searchText.getText().toString());
          
        	String [] assetPaths = null;
	      	try {
	  			 assetPaths = getAssets().list("");
	  		} catch (IOException e1) {
	  			// TODO Auto-generated catch block
	  			e1.printStackTrace();
	  		}
	      	
	  		String key = searchText.getText().toString();
	  		String definition = "none";
	  		
	      	ModdedCdb db = null;
	  		
	      	// images, sounds, webkit are first 3 - but always?
	      	
	      	for(int i = 3; i < assetPaths.length;i++) {	
	  	    
	      		try {
	  				db = new ModdedCdb(getAssets().open(assetPaths[i]));
	  			} catch (IOException e) {
	  				e.printStackTrace();
	  			}
	  			
	  			byte[] bytes = db.find(key.getBytes());
	  		
	  			if (bytes != null) {
	  				definition = new String(bytes);
	  				break;
	  			}
	      	}
	  				
	  		db.close();
          
	  		DictionaryActivity.searchResultsPage.loadData(definition, "text/html", "utf-8");
        }
    };

}
