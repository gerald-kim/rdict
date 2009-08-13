package com.amplio.rdict;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import android.app.Activity;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TabHost;

import com.amplio.rdict.history.HistoryManager;
import com.amplio.rdict.review.CardSetManager;
import com.amplio.rdict.review.ReviewManager;
import com.amplio.rdict.review.StatisticsManager;
import com.amplio.rdict.search.AssetInputStreamProvider;
import com.amplio.rdict.search.Dictionary;
import com.amplio.rdict.setup.DictionaryDownloader;
import com.amplio.rdict.setup.SetupActivity;
import com.amplio.rdict.setup.SetupManager;

public class RDictActivity extends TabActivity implements  AssetInputStreamProvider {
	
	private static final int TAB_INDEX_SEARCH = 0;
	private static final int TAB_INDEX_REVIEW = 1;
	private static final int TAB_INDEX_HISTORY = 2;
	private static final int TAB_INDEX_MORE = 3;
	
	private static final String BASE_PACKAGE = "com.amplio.rdict";
	
	private static final String[] ACTIVITY_PATHS = {BASE_PACKAGE + ".search.",
													BASE_PACKAGE + ".review.",
													BASE_PACKAGE + ".history.",
													BASE_PACKAGE + ".more."};
	private static final String[] TAB_NAMES = { "Search", "Review", "History", "More"};

	TabHost.TabSpec searchTab = null;
	TabHost.TabSpec reviewTab = null;
	TabHost.TabSpec historyTab = null;
	TabHost.TabSpec moreTab = null;
	
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
    	
    	this.db_file = new File(DictionaryDownloader.WRITE_PATH_DB);
        
    	SetupActivity.setupMgr = new SetupManager();
		this.setupActivityIntent = new Intent(this.getApplicationContext(), SetupActivity.class);
    	
        if(this.db_file.exists()) {
        	setContentView(R.layout.main);
        	initDatabaseManagers();
        	setupTabs(this.getTabHost());
        }
        else {
        	SetupActivity.setupMgr = new SetupManager();
    		this.setupActivityIntent = new Intent(this.getApplicationContext(), SetupActivity.class);
        }
    }
    
    private void setupTabs(TabHost tabHost) {
    	this.searchTab = this.createTab(tabHost, TAB_INDEX_SEARCH);
    	tabHost.addTab(this.searchTab);
    	
    	this.reviewTab = this.createTab(tabHost, TAB_INDEX_REVIEW);
    	tabHost.addTab(this.reviewTab);
    	
    	this.historyTab = this.createTab(tabHost, TAB_INDEX_HISTORY);
    	tabHost.addTab(this.historyTab);
    	
    	this.moreTab = this.createTab(tabHost, TAB_INDEX_MORE);
    	tabHost.addTab(this.moreTab);
        
        this.setDefaultTab(TAB_INDEX_SEARCH);
    }
    
    public TabHost.TabSpec createTab(TabHost tabHost, int index) {
    	TabHost.TabSpec tab = tabHost.newTabSpec(TAB_NAMES[index]);
    	ComponentName activity = new ComponentName(BASE_PACKAGE, ACTIVITY_PATHS[index] + TAB_NAMES[index] + "Activity");
    	tab.setContent(new Intent().setComponent(activity));
    	
    	if(index == TAB_INDEX_REVIEW){
    		StringBuilder sb = new StringBuilder(TAB_NAMES[index]);
    		
    		int cardsScheduledForToday = c_cardSetManager.loadCardsScheduledForToday().size();
    		if(0 < cardsScheduledForToday) {
	    		sb.append("(");
	    		sb.append(cardsScheduledForToday);
	    		sb.append(")");
    		}
    		
    		tab.setIndicator(sb.toString());
    	}
    	else {
    		tab.setIndicator(TAB_NAMES[index]);
    	}
    	
    	return tab;
    }
    
    public void onResume() {
		super.onResume();
		
		System.out.println("RDict - On Resume");
		
		if(! this.db_file.exists() ){
			if (! this.userChoseToDelaySetup())
				this.startActivity(this.setupActivityIntent);
			else
				this.finish();
		}
		else if(this.db_file.exists() && ! this.isInittedDatabaseManagers){
			initDatabaseManagers();
			setupTabs(this.getTabHost());
		}
	}
    
    @Override
    protected void onDestroy() {
    	System.out.println("RDict - On Destroy");
    	
    	if(m_db != null)
    		m_db.close();    	
    	
    	if(m_con != null)
    		m_con.close();
    	
    	super.onDestroy();
    }
    
    public boolean userChoseToDelaySetup() {
		return SetupManager.STATE_SETUP_DELAYED == SetupActivity.setupMgr.getState();
	}   
    
	private void initDatabaseManagers() {
		if(m_db == null || m_db.isClosed())
		    m_db = ODBFactory.open( this.getApplicationContext().getFilesDir() + "/" + "rdict_db.odb" );
	    
	    c_cardSetManager = new CardSetManager( m_db );
	    c_reviewManager = new ReviewManager( m_db, c_cardSetManager );
	    c_statisticsManager = new StatisticsManager( m_db, c_cardSetManager );
        
	    if(m_con == null || ! m_con.isOpen())
	    	m_con = SQLiteDatabase.openDatabase("/sdcard/rdict/word.db", null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
    	
	    c_historyMgr = new HistoryManager(m_con);
		c_historyMgr.createTableIfNotExists( m_con );
		c_dictionary = new Dictionary( "/sdcard/rdict/word.cdb", "/sdcard/rdict/word.index",
		        getAssetInputStream( "dictionary_js.html" ) );
	
    	this.isInittedDatabaseManagers = true;
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