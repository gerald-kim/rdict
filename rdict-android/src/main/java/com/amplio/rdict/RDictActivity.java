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
import com.amplio.rdict.review.CardSetManager;
import com.amplio.rdict.review.ReviewManager;
import com.amplio.rdict.review.StatisticsManager;

public class RDictActivity extends TabActivity {
	
	private static final String BASE_PACKAGE = "com.amplio.rdict";
	
	private static final String[] ACTIVITY_PATHS = {BASE_PACKAGE + ".search.",
													BASE_PACKAGE + ".review.",
													BASE_PACKAGE + ".history.",
													BASE_PACKAGE + ".more."};
	private static final String[] TABS = { "Search", "Review", "History", "More"};
	private ODB m_db = null;
	//test

	public static HistoryManager c_historyMgr = null;
	public static CardSetManager c_cardSetManager = null;
	public static StatisticsManager c_statisticsManager = null;
	public static ReviewManager c_reviewManager = null;

	private SQLiteDatabase m_con;
	
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
        
		initDatabaseManagers();
    }

	private void initDatabaseManagers() {
	    m_db = ODBFactory.open( this.getApplicationContext().getFilesDir() + "/"
		        + "rdict_db.odb" );
	    
	    c_cardSetManager = new CardSetManager( m_db );
	    c_reviewManager = new ReviewManager( m_db, c_cardSetManager );
	    c_statisticsManager = new StatisticsManager( m_db, c_cardSetManager );
        
        m_con = SQLiteDatabase.openDatabase("/sdcard/rdict/word.db", null, SQLiteDatabase.OPEN_READWRITE);
    	c_historyMgr = new HistoryManager(m_con);
    	
    	c_historyMgr.createTableIfNotExists(m_con);
    }
    
    @Override
    protected void onDestroy() {
//    	m_db.commit();
    	m_db.close();
    	m_con.close();
    	super.onDestroy();
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