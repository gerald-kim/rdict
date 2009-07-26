package com.amplio.rdict.history;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.amplio.rdict.R;
import com.amplio.rdict.search.DictionaryActivity;
import com.amplio.rdict.search.DictionaryEntry;
import com.amplio.rdict.search.SearchActivity;

public class HistoryActivity extends Activity implements OnItemClickListener {
	
	private ListView _historyList = null;
	
	private HistoryManager _historyMgr = null;
	
	private Vector<DictionaryEntry> words = null;
	
	public Map<String,?> createItem(String title, String caption) {
		Map<String,String> item = new HashMap<String,String>();
		item.put("title", title);
		item.put("caption", caption);
		return item;
	}
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.history);
		
		_historyList = (ListView) findViewById(R.id.history_list);
		_historyList.setOnItemClickListener(this);
		
		SQLiteDatabase con = SQLiteDatabase.openDatabase("/sdcard/rdict/word.db", null, SQLiteDatabase.OPEN_READONLY);
    	_historyMgr = new HistoryManager(con);
	}
	
	public void onResume() {
		super.onResume();
		
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		
		String[] strings;
		
		for(int i = 0; i < 5; i++) {
			words = _historyMgr.loadHistoryRecordsByDate(sdf2.format(cal.getTime()));
			
			if(words.size() > 0 ) {
				strings = new String[words.size()];
				for(int j = 0; j < words.size(); j++) {
					strings[j] = words.get(j).headword;
					
					System.out.println(strings[j]);
				}
				
				adapter.addSection(sdf.format(cal.getTime()), new ArrayAdapter<String>(this,
																					  android.R.layout.simple_list_item_1,
																					  strings));
			}
			
			cal.add(Calendar.HOUR, -24);
		}
		
		_historyList.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		String word = parent.getAdapter().getItem(position).toString();
		
		SearchActivity.searchWord = new DictionaryEntry(word, "something");
		SearchActivity.searchHistory.clear();
					
		Intent i = new Intent(this.getApplicationContext(), DictionaryActivity.class);
		this.startActivity(i);
	}
}
