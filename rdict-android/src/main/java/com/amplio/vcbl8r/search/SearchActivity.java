package com.amplio.vcbl8r.search;

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

import com.amplio.vcbl8r.R;
import com.amplio.vcbl8r.RDictActivity;
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
    	if(RDictActivity.dictionary != null && Dictionary.words != null) {
    		this.updateWordList();
    		
    		int wordIndex = RDictActivity.dictionary.findWordIndex( "a" );
    		
    		if (! "".equals(searchText.toString().trim()))
    			wordIndex = RDictActivity.dictionary.findWordIndex(searchText.getText().toString());
    		
    		_wordList.setSelectionFromTop(wordIndex, 0 );
    	}
    	
    	super.onResume();
    }
    
   	public void onTextChanged(CharSequence s, int start, int before, int count) {
   		if(s != null && RDictActivity.dictionary != null){  // bad hack... why is this happening?
	   		int wordIndex = RDictActivity.dictionary.findWordIndex( s.toString() );
			_wordList.setSelectionFromTop( wordIndex, 0 );
   		}
	}
   	
   	public void afterTextChanged(Editable s) {}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
		String headword = Dictionary.words[parent.getPositionForView(view)];
		
		RDictActivity.historyMgr.addHistoryRecord(headword);
		
		DictionaryEntry entry = RDictActivity.dictionary.searchByWord(headword);
		
		DictionaryActivity.sessionHistory.clear();
		DictionaryActivity.sessionHistory.addWord(entry);
		
		Intent i = new Intent(this.getApplicationContext(), DictionaryActivity.class);
		i.putExtra(INTENT_PARAM_HEADWORD, entry.headword);
		i.putExtra(INTENT_PARAM_CONTENTS, entry.contents);
		
		this.startActivity(i);
	}
}
