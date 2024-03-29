package com.amplio.vkbl8r;

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

import com.amplio.vkbl8r.history.HistoryManager;
import com.db4o.ObjectContainer;

public class RDictActivity extends TabActivity {
	
	private static final String BASE_PACKAGE = "com.amplio.vkbl8r";
	
	private static final String[] ACTIVITY_PATHS = {BASE_PACKAGE + ".search.",
													BASE_PACKAGE + ".review.",
													BASE_PACKAGE + ".history.",
													BASE_PACKAGE + ".more."};
	
	private static final String[] TABS = { "Search", "Review", "History", "More"};
	public static ObjectContainer db = null;

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
        
        RDictActivity.db = DB4oConnectionFactory.getObjectContainer(this.getApplicationContext().getFilesDir() + "/" + "vkbl8r_db.db4o");
        
        SQLiteDatabase con = SQLiteDatabase.openDatabase("/sdcard/vkbl8r/word.db", null, SQLiteDatabase.OPEN_READWRITE);
    	_historyMgr = new HistoryManager(con);
    	
    	_historyMgr.createTableIfNotExists(con);
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