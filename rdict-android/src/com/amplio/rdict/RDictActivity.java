package com.amplio.rdict;

import java.io.IOException;

import com.strangegizmo.cdb.ModdedCdb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RDictActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	String [] assetPaths = null;
    	try {
			 assetPaths = getAssets().list("");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
		String key = "you";
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
		
    	super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText(definition);
		setContentView(tv);
    }
}