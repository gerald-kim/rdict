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
		Cursor c = _con.rawQuery("select def from word_db where word = ? limit 1", new String[]{word});
		
		if(0 < c.getCount()){
			c.moveToNext();
			return _factory.makeHTMLifiedEntry(word, c.getString(0));
		}
  		else {
  			return null;
  		}
	}
	
	public DictionaryEntry getWordByID(long id) {
		Cursor c = _con.rawQuery("select word, def from word_db where _id = " + id, null);
		
		if(0 < c.getCount()){
			c.moveToNext();
			return _factory.makeHTMLifiedEntry(c.getString(0), c.getString(1));
		}
  		else {
  			return null;
  		}
	}
	
	public Vector<Headword> findMatchingWords(String str) {
		Vector<Headword> matches = new Vector<Headword>();
		Cursor c = _con.rawQuery("select * from word_db where word GLOB '" + str + "*' limit 50", null);
		
		for(int i = 0; i < c.getCount(); i++){
			c.moveToNext();
			matches.add(new Headword(c.getLong(0), c.getString(1), c.getString(2)));
		}
  		
		return matches;
	}
}
