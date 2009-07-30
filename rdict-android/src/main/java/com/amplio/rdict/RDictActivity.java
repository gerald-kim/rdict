package com.amplio.rdict;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import android.app.Activity;
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
import com.amplio.rdict.search.AssetInputStreamProvider;
import com.amplio.rdict.search.Dictionary;
import com.amplio.rdict.setup.DownloadManager;
import com.amplio.rdict.setup.SetupActivity;
import com.amplio.rdict.setup.SetupManager;

public class RDictActivity extends TabActivity implements  AssetInputStreamProvider {
	
	private static final String BASE_PACKAGE = "com.amplio.rdict";
	
	private static final String[] ACTIVITY_PATHS = {BASE_PACKAGE + ".search.",
													BASE_PACKAGE + ".review.",
													BASE_PACKAGE + ".history.",
													BASE_PACKAGE + ".more."};
	private static final String[] TABS = { "Search", "Review", "History", "More"};

	private File db_file = null;
	private ODB m_db = null;
	private SQLiteDatabase m_con = null;
	
	private Intent setupActivityIntent = null;
	
	private boolean isInittedDatabaseManagers = false;

	public static Dictionary c_dictionary = null;
	public static HistoryManager c_historyMgr = null;
	public static CardSetManager c_cardSetManager = null;
	public static StatisticsManager c_statisticsManager = null;
	public static ReviewManager c_reviewManager = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setDefaultTab(0);
    	
        TabHost tabs = getTabHost();
    	
        for (int i = 0; i < TABS.length; i++) {
        	TabHost.TabSpec tab = tabs.newTabSpec(TABS[i]);
        	
        	ComponentName activity = new ComponentName(BASE_PACKAGE, ACTIVITY_PATHS[i] + TABS[i] + "Activity");

        	tab.setContent(new Intent().setComponent(activity));
        	tab.setIndicator(TABS[i]);
        	tabs.addTab(tab);
        }
        
        SetupActivity.setupMgr = new SetupManager();
		this.setupActivityIntent = new Intent(this.getApplicationContext(), SetupActivity.class);
        
        this.db_file = new File(DownloadManager.WRITE_PATH);
        
		if(! this.db_file.exists() ){
			this.startActivity(this.setupActivityIntent);
		}
		else {
			initDatabaseManagers();
		}
    }
    
    public void onResume() {
		super.onResume();
		
		if(! this.db_file.exists() ){
			if (this.userChoseToDelaySetup())
				this.finish();
			else
				this.startActivity(this.setupActivityIntent);
		}
		
		else if(this.db_file.exists() && ! this.isInittedDatabaseManagers){
			initDatabaseManagers();
		}
	}
	
	public boolean userChoseToDelaySetup() {
		return SetupManager.STATE_SETUP_DELAYED == SetupActivity.setupMgr.getState();
	}   

	private void initDatabaseManagers() {
		if(m_db == null)
		    m_db = ODBFactory.open( this.getApplicationContext().getFilesDir() + "/" + "rdict_db.odb" );
	    
	    c_cardSetManager = new CardSetManager( m_db );
	    c_reviewManager = new ReviewManager( m_db, c_cardSetManager );
	    c_statisticsManager = new StatisticsManager( m_db, c_cardSetManager );
        
	    if(m_con == null)
	    	m_con = SQLiteDatabase.openDatabase("/sdcard/rdict/word.db", null, SQLiteDatabase.OPEN_READWRITE);
    	
	    c_historyMgr = new HistoryManager(m_con);
    	c_historyMgr.createTableIfNotExists(m_con);
    	c_dictionary = new Dictionary( m_con, getAssetInputStream("dictionary_js.html") );
    	
    	this.isInittedDatabaseManagers = true;
    }
    
    @Override
    protected void onDestroy() {
    	m_db.commit();
//    	if(m_db != null)
//    		m_db.close();
//    	if(m_con != null)
//    		m_con.close();
    	
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
	
	public InputStream getAssetInputStream(String path) {
		return RDictActivity.getAssetInputStream(this, path );
	}
	
	public static InputStream getAssetInputStream(Activity activity, String path) {
		InputStream stream = null;
		try {
			stream = activity.getAssets().open(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stream;
	}

}