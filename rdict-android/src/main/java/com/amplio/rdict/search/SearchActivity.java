package com.amplio.rdict.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.history.History;

public class SearchActivity extends Activity implements AssetInputStreamProvider, TextWatcher, OnItemClickListener {
	private EditText searchText;
	private ListView _wordList;
	public static DictionaryEntry searchWord = null;
	public Vector<DictionaryEntry> words = null;
	
	public static History searchHistory = new History();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);		
		setContentView(R.layout.search);
		
		this.searchText = (EditText)findViewById(R.id.widget43);
		this.searchText.addTextChangedListener(this);
		
		_wordList = (ListView) findViewById(R.id.listview);
		_wordList.setOnItemClickListener(this);
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
		words = RDictActivity.c_dictionary.findMatchingWords(s.toString());
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
		
		for(int i = 0; i < words.size(); i++)
			aa.add(words.get(i).headword);

		_wordList.setAdapter(aa);
		aa.notifyDataSetChanged();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int index = parent.getPositionForView(view);
		
		SearchActivity.searchWord = words.elementAt(index);
		
		RDictActivity.c_historyMgr.addHistoryRecord(SearchActivity.searchWord.headword);
		System.out.println("Saved " + SearchActivity.searchWord.headword);
		
		SearchActivity.searchHistory.clear();
					
		Intent i = new Intent(this.getApplicationContext(), DictionaryActivity.class);
		this.startActivity(i);
	}
}
