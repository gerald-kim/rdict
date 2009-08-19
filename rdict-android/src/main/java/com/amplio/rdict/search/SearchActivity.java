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
	public static final String INTENT_PARAM_HEADWORD = "headword";
	public static final String INTENT_PARAM_CONTENTS = "contents";
	
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
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, new String[]{});
		_wordList.setAdapter(aa);
		aa.notifyDataSetChanged();
    }
    
	public void updateWordList(){
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Dictionary.words);
		_wordList.setAdapter(aa);
		aa.notifyDataSetChanged();
	}
    
    public void onResume(){
    	if(Dictionary.words != null) {
    		this.updateWordList();
    		_wordList.setSelectionFromTop( RDictActivity.c_dictionary.findWordIndex( "a" ), 0 );
    	}
    		
    	System.out.println("Dic resumed.");
    	
    	super.onResume();
    }
    
   	public void onTextChanged(CharSequence s, int start, int before, int count) {
   		int wordIndex = RDictActivity.c_dictionary.findWordIndex( s.toString() );
		_wordList.setSelectionFromTop( wordIndex, 0 );
	}
   	
   	public void afterTextChanged(Editable s) {}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
		String headword = Dictionary.words[parent.getPositionForView(view)];
		
		RDictActivity.c_historyMgr.addHistoryRecord(headword);
		
		DictionaryEntry entry = RDictActivity.c_dictionary.searchByWord(headword);
		
		DictionaryActivity.sessionHistory.clear();
		DictionaryActivity.sessionHistory.addWord(entry);
		
		Intent i = new Intent(this.getApplicationContext(), DictionaryActivity.class);
		i.putExtra(INTENT_PARAM_HEADWORD, entry.headword);
		i.putExtra(INTENT_PARAM_CONTENTS, entry.contents);
		
		this.startActivity(i);
	}
}
