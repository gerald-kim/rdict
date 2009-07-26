package com.amplio.rdict;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.amplio.rdict.history.HistoryManager;

public class RDictActivity extends TabActivity {
	
	private static final String BASE_PACKAGE = "com.amplio.rdict";
	
	private static final String[] ACTIVITY_PATHS = {BASE_PACKAGE + ".search.",
													BASE_PACKAGE + ".review.",
													BASE_PACKAGE + ".history.",
													BASE_PACKAGE + ".more."};
	private static final String[] TABS = { "Search", "Review", "History", "More"};
	public static ODB db = null;
	//test
	private HistoryManager _historyMgr = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setDefaultTab(0);
    	
        TabHost tabs = getTabHost();
    	
        for (int i = 0; i < TABS.length; i++){
        	TabHost.TabSpec tab = tabs.newTabSpec(TABS[i]);
        	
        	ComponentName activity = new ComponentName(BASE_PACKAGE, ACTIVITY_PATHS[i] + TABS[i] + "Activity");

        	tab.setContent(new Intent().setComponent(activity));
        	tab.setIndicator(TABS[i]);
        	tabs.addTab(tab);
        }
        
		RDictActivity.db = ODBFactory.open( this.getApplicationContext().getFilesDir() + "/"
		        + "rdict_db.odb" );
        
        SQLiteDatabase con = SQLiteDatabase.openDatabase("/sdcard/rdict/word.db", null, SQLiteDatabase.OPEN_READWRITE);
    	_historyMgr = new HistoryManager(con);
    	
    	_historyMgr.createTableIfNotExists(con);
    }
    
    @Override
    protected void onDestroy() {
    	db.commit();
    	db.close();
    }


	public static class MyTabIndicator extends LinearLayout {
		public MyTabIndicator(Context context, String label) {
			super(context);
			
			View tab = View.inflate(this.getContext(), R.layout.tab_indicator, null);

			TextView tv = (TextView)tab.findViewById(R.id.tab_label);
			tv.setText(label);
		}
		
    }
}