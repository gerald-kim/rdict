package com.amplio.vkbl8r;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import com.amplio.vkbl8r.setup.SetupActivity;

public class MainActivity extends Activity {
	SQLiteDatabase con = null;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	SetupActivity.hasBeenRun = false;
    	
    	try {
			this.con = SQLiteDatabase.openDatabase("/sdcard/vkbl8r/word.db", null, SQLiteDatabase.OPEN_READWRITE);
			Intent i = new Intent(this.getApplicationContext(), RDictActivity.class);	
			this.startActivity(i);
		}
		catch(SQLiteException ignore){
			if(! SetupActivity.hasBeenRun) {
				Intent i = new Intent(this.getApplicationContext(), SetupActivity.class);
				this.startActivity(i);
			}
			
			try {
				this.con = SQLiteDatabase.openDatabase("/sdcard/vkbl8r/word.db", null, SQLiteDatabase.OPEN_READWRITE);
				Intent i = new Intent(this.getApplicationContext(), RDictActivity.class);	
				this.startActivity(i);
			}
			catch(SQLiteException ignore2){
				this.finish();
			}
		}
    }

	public void onResume() {
		super.onResume();
	}
}
