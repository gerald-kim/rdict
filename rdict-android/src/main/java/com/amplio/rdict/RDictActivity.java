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
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

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
	
	public static final int TAB_INDEX_SEARCH = 0;
	public static final int TAB_INDEX_REVIEW = 1;
	public static final int TAB_INDEX_HISTORY = 2;
	public static final int TAB_INDEX_MORE = 3;
	
	private static final String BASE_PACKAGE = "com.amplio.rdict";
	
	private static final String[] ACTIVITY_PATHS = {BASE_PACKAGE + ".search.",
													BASE_PACKAGE + ".review.",
													BASE_PACKAGE + ".history.",
													BASE_PACKAGE + ".more."};
	private static final String[] TAB_NAMES = { "Search", "Review", "History", "More"};

	private static final int[] TAB_ICONS = { R.drawable.ic_menu_search, 
												R.drawable.ic_menu_slideshow,
												R.drawable.ic_menu_recent_history,
												R.drawable.ic_menu_manage };
	
	public static RDictActivity RDICT_ACTIVITY = null;
	
	TabHost tabHost = null;
	TabHost.TabSpec searchTab = null;
	TabHost.TabSpec reviewTab = null;
	TabHost.TabSpec historyTab = null;
	TabHost.TabSpec moreTab = null;
	
	public static ODB odb = null;
	public static SQLiteDatabase con = null;
	
	public static StartupManager startupMgr = null;
	
	public static Dictionary dictionary = null;
	public static HistoryManager historyMgr = null;
	public static CardSetManager cardSetManager = null;
	public static StatisticsManager statisticsManager = null;
	public static ReviewManager reviewManager = null;
	
	boolean didRunSetup = false;
	boolean didTryToLoadDict = false;
	public static boolean didLoadDict = false;
	boolean didInitMgrs = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.main);
    	tabHost = getTabHost();
		setupTabs(tabHost);
    	
    	SetupActivity.setupMgr = new SetupManager();
    	startupMgr = new StartupManager();
    	
    	RDICT_ACTIVITY = this;
    }
    
    public void onResume() {
    	doAction(getAction());
    	
    	super.onResume();
	}
    
    public int getAction() {
    	return StartupManager.getAction(existNecessaryDataFiles(), didRunSetup, didTryToLoadDict, RDictActivity.didLoadDict, didInitMgrs);
    }
    
    private boolean existNecessaryDataFiles() {
    	File dbFile = new File(DictionaryDownloader.WRITE_PATH_DB);
    	File indexFile = new File(DictionaryDownloader.WRITE_PATH_INDEX);
    	return dbFile.exists() && indexFile.exists();
    }
    
    public void doAction(int action) {
    	switch (action) {
	    	case StartupManager.ACTION_DO_SETUP_ACTIVITY:
	    		startActivity(new Intent(getApplicationContext(), SetupActivity.class));
	    		didRunSetup = true;
	    		break;
	    	
	    	case StartupManager.ACTION_FINISH_USER_DELAYED_OR_CANCELLED_SETUP:
	    		finish();
	    		break;
	    		
	    	case StartupManager.ACTION_FINISH_USER_PRESSED_BACK_BUTTON:
	    		
//	    		stop service
//	    		
//	    		delete files
	    		
	    		finish();
	    		break;
	    		
	    	case StartupManager.ACTION_DO_LOAD_DICT_SERVICE:
	    		this.didTryToLoadDict = true;
	    		startActivity(new Intent(getApplicationContext(), SplashActivity.class));
	    		break;
	    		
	    	case StartupManager.ACTION_DO_INIT_MANAGERS:
	    		initDatabaseManagers();
	    		
	    		updateReviewTabIndicator();
	    		
	    		didInitMgrs = true;
	    		break;
	    		
	    	case StartupManager.ACTION_DO_RDICT_ACTIVITY:
	    		RDictActivity.RDICT_ACTIVITY.updateReviewTabIndicator();
	    		break;
    	}
    }
    
    private void setupTabs(TabHost tabHost) {
    	searchTab = createTab(tabHost, TAB_INDEX_SEARCH);
    	tabHost.addTab(searchTab);
    	
    	reviewTab = createTab(tabHost, TAB_INDEX_REVIEW);
    	tabHost.addTab(reviewTab);
    	
    	historyTab = createTab(tabHost, TAB_INDEX_HISTORY);
    	tabHost.addTab(historyTab);
    	
    	moreTab = createTab(tabHost, TAB_INDEX_MORE);
    	tabHost.addTab(moreTab);
        
        setDefaultTab(TAB_INDEX_SEARCH);
    }
    
    public TabHost.TabSpec createTab(TabHost tabHost, int index) {
    	TabHost.TabSpec tab = tabHost.newTabSpec(TAB_NAMES[index]);
    	ComponentName activity = new ComponentName(BASE_PACKAGE, ACTIVITY_PATHS[index] + TAB_NAMES[index] + "Activity");
    	tab.setContent(new Intent().setComponent(activity));
    	
    	if(index == TAB_INDEX_REVIEW && cardSetManager != null && odb != null && ! odb.isClosed())
		    tab.setIndicator(buildReviewTabIndicator(), getResources().getDrawable(TAB_ICONS[index]));
    	else {
    		tab.setIndicator(TAB_NAMES[index], getResources().getDrawable(TAB_ICONS[index]));
    	}
    	
    	return tab;
    }

	public void updateReviewTabIndicator() {
	    RelativeLayout r = (RelativeLayout) tabHost.getTabWidget().getChildAt(1);
	    TextView reviewTabTextView = (TextView) r.getChildAt(1);  // hackmaster Steve -_-;;;
	    reviewTabTextView.setText(buildReviewTabIndicator());
    }

	private String buildReviewTabIndicator() {
	    StringBuilder sb = new StringBuilder(TAB_NAMES[TAB_INDEX_REVIEW]);
	    
	    int cardsScheduledForToday = cardSetManager.countCardsScheduledForToday();
	    if(0 < cardsScheduledForToday) {
	    	sb.append("(");
	    	sb.append(cardsScheduledForToday);
	    	sb.append(")");
	    }
	    return sb.toString();
    }
    
	
    @Override
    protected void onDestroy() {
    	System.out.println("RDict - On Destroy");
    	
    	if(odb != null && ! odb.isClosed())
    		odb.close();
    	
    	if(con != null && con.isOpen())
    		con.close();
    	
    	super.onDestroy();
    }
    
	private void initDatabaseManagers() {
		if(odb == null || odb.isClosed())
		    odb = ODBFactory.open( getApplicationContext().getFilesDir() + "/" + "rdict_db.odb" );
	    
	    cardSetManager = new CardSetManager( odb );
	    reviewManager = new ReviewManager( odb, cardSetManager );
	    statisticsManager = new StatisticsManager( odb, cardSetManager );
        
	    if(con == null || ! con.isOpen())
	    	con = SQLiteDatabase.openDatabase("/sdcard/rdict/word.db", null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
    	
	    historyMgr = new HistoryManager(con);
		historyMgr.createTableIfNotExists( con );
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