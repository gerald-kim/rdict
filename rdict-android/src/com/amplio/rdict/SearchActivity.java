package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity implements AssetInputStreamProvider, TextWatcher, OnItemClickListener {
	private EditText searchText;
	private ListView _wordList;
	private Dictionary _dictionary = null;
	private HistoryManager _historyMgr = null;
	
	public static Word searchWord = null;
	public Vector<Word> words = null;
	
	public static History searchHistory = new History();
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);		
		setContentView(R.layout.search);
		
		this.searchText = (EditText)findViewById(R.id.widget43);
		this.searchText.addTextChangedListener(this);
		
		_wordList = (ListView) findViewById(R.id.listview);
		_wordList.setOnItemClickListener(this);
		
		SQLiteDatabase con = SQLiteDatabase.openDatabase("/sdcard/rdict/word.db", null, SQLiteDatabase.OPEN_READWRITE);
    	_dictionary = new Dictionary(con, getAssetInputStream("dictionary_js.html"));
    	_historyMgr = new HistoryManager(con);
    }
    
    public void onStart(){
    	super.onStart();
    	
    	System.out.println("Dic started ");
    }
    
    public void onResume(){
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
    }
    
    public void onPause() {
    	System.out.println("Dic paused;");
    	super.onPause();
    }
    
    public void onStop(){
    	System.out.println("Dic stopped;");
    	
    	super.onStop();
    }
    
	public InputStream getAssetInputStream(String path) {
		InputStream stream = null;
		try {
			stream = this.getAssets().open(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stream;
	}
	
	public void afterTextChanged(Editable s) {
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		words = this._dictionary.findMatchingWords(s.toString());
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
		
		for(int i = 0; i < words.size(); i++)
			aa.add(words.get(i)._word);

		_wordList.setAdapter(aa);
		aa.notifyDataSetChanged();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int index = parent.getPositionForView(view);
		
		SearchActivity.searchWord = words.elementAt(index);
		
		_historyMgr.addHistoryRecord(SearchActivity.searchWord._word);
		System.out.println("Saved " + SearchActivity.searchWord._word);
		
		SearchActivity.searchHistory.clear();
					
		Intent i = new Intent(this.getApplicationContext(), DictionaryActivity.class);
		this.startActivity(i);
	}
}
