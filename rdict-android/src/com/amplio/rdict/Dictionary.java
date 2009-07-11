package com.amplio.rdict;

import java.io.InputStream;
import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;

public class Dictionary {
	private DictionaryEntryFactory _factory = null;
	private SQLiteDatabase _con = null;
	
	public Dictionary(SQLiteDatabase con, InputStream htmlStream) {
		_con = con;
		_factory = new DictionaryEntryFactory(htmlStream);
	}
	
	public DictionaryEntry searchByWord(String word) {
		Cursor c = _con.rawQuery("select def from word_db where word GLOB '" +  word + "' limit 1", null);
		
		if(0 < c.getCount()){
			c.moveToNext();
			return _factory.makeHTMLifiedEntry(word, c.getString(0));
		}
  		else {
  			return null;
  		}
	}
	
	public Vector<String> findMatchingWords(String str) {
		Vector<String> matches = new Vector<String>();
		Cursor c = _con.rawQuery("select word from word_db where word GLOB '" + str + "*' limit 10", null);
		
		for(int i = 0; i < c.getCount(); i++){
			c.moveToNext();
			matches.add(c.getString(0));
		}
  		
		return matches;
	}
}
