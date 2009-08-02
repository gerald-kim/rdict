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
// this should only deal with headwords
public class SearchActivity extends Activity implements AssetInputStreamProvider, TextWatcher, OnItemClickListener {
	private EditText searchText;
	private ListView _wordList;
	public Vector<String> headwords = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);		
		setContentView(R.layout.search);
		
		this.searchText = (EditText)findViewById(R.id.widget43);
		this.searchText.addTextChangedListener(this);
		
		_wordList = (ListView) findViewById(R.id.listview);
		_wordList.setOnItemClickListener(this);
    }
    
    public void onResume(){
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
    }
    
   	public void onTextChanged(CharSequence s, int start, int before, int count) {
		headwords = RDictActivity.c_dictionary.findMatchingHeadwords(s.toString());
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
		
		for(int i = 0; i < headwords.size(); i++)
			aa.add(headwords.get(i));

		_wordList.setAdapter(aa);
		aa.notifyDataSetChanged();
	}
   	
   	public void afterTextChanged(Editable s) {
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String headword = headwords.elementAt(parent.getPositionForView(view));
		
		RDictActivity.c_historyMgr.addHistoryRecord(headword);
		
		DictionaryActivity.dicEntry = RDictActivity.c_dictionary.searchByWord(headword);
		DictionaryActivity.sessionHistory.clear();
		DictionaryActivity.sessionHistory.addWord(DictionaryActivity.dicEntry);
		
		Intent i = new Intent(this.getApplicationContext(), DictionaryActivity.class);
		this.startActivity(i);
	}
	
	public void onStart(){
    	super.onStart();
    	
    	System.out.println("Dic started ");
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
}
