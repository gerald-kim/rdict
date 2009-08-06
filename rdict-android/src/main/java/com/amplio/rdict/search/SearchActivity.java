package com.amplio.rdict.search;

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
public class SearchActivity extends Activity implements TextWatcher, OnItemClickListener {
	private EditText searchText;
	private ListView _wordList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);		
		setContentView(R.layout.search);
		
		this.searchText = (EditText)findViewById(R.id.widget43);
		this.searchText.addTextChangedListener(this);
		
		_wordList = (ListView) findViewById(R.id.listview);
		_wordList.setOnItemClickListener(this);

		ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, RDictActivity.c_dictionary.words);
		_wordList.setAdapter(aa);
		aa.notifyDataSetChanged();
		
		_wordList.setSelectionFromTop( RDictActivity.c_dictionary.findWordIndex( "a" ), 0 );
    }
    
    public void onResume(){
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
    }
    
   	public void onTextChanged(CharSequence s, int start, int before, int count) {
   		
   		_wordList.setSelectionFromTop( RDictActivity.c_dictionary.findWordIndex( s.toString() ), 0 );
	}
   	
   	public void afterTextChanged(Editable s) {
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
		String headword = RDictActivity.c_dictionary.words[parent.getPositionForView( view )];
		
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
}
