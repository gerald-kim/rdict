package com.amplio.rdict.history;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.search.DictionaryActivity;
import com.amplio.rdict.search.DictionaryEntry;
import com.amplio.rdict.search.SearchActivity;

public class HistoryActivity extends Activity implements OnItemClickListener {
	
	private ListView m_historyListView = null;
	
	private Vector<DictionaryEntry> words = null;
	
	public Map<String,String> createItem(String title, String caption) {
		Map<String,String> item = new HashMap<String,String>();
		item.put("title", title);
		item.put("caption", caption);
		return item;
	}
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.history);
		
		m_historyListView = (ListView) findViewById(R.id.history_list);
		m_historyListView.setOnItemClickListener(this);
	}
	
	public void onResume() {
		super.onResume();
		
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		
		String[] strings;
		
		boolean didAddHistoryRecord = false;
		for(int i = 0; i < 5; i++) {
			words = RDictActivity.c_historyMgr.loadHistoryRecordsByDate(sdf2.format(cal.getTime()));
			
			if(words.size() > 0 ) {
				strings = new String[words.size()];
				for(int j = 0; j < words.size(); j++) {
					strings[j] = words.get(j).headword;
				}
				
				adapter.addSection(sdf.format(cal.getTime()), new ArrayAdapter<String>(this,
																					  android.R.layout.simple_list_item_1,
																					  strings));
				
				didAddHistoryRecord = true;
			}
			
			cal.add(Calendar.HOUR, -24);
		}
		
		if(! didAddHistoryRecord) {
			adapter.addSection("No history records.", new ArrayAdapter<String>(this,
																				android.R.layout.simple_list_item_1,
																				new String[]{}));
		}
		
		m_historyListView.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String word = parent.getAdapter().getItem(position).toString();
		
		DictionaryEntry entry = RDictActivity.c_dictionary.searchByWord(word);
		
		DictionaryActivity.sessionHistory.clear();
		DictionaryActivity.sessionHistory.addWord(entry);
		
		Intent i = new Intent(this.getApplicationContext(), DictionaryActivity.class);
		i.putExtra(SearchActivity.INTENT_PARAM_HEADWORD, entry.headword);
		i.putExtra(SearchActivity.INTENT_PARAM_CONTENTS, entry.contents);
		
		this.startActivity(i);
	}
}
