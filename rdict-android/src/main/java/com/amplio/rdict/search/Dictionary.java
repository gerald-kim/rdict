package com.amplio.rdict.search;

import java.io.InputStream;
import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Dictionary {
	private DictionaryEntryFactory _factory = null;
	private SQLiteDatabase _con = null;
	
	public Dictionary(SQLiteDatabase con, InputStream htmlStream) {
		_con = con;
		_factory = new DictionaryEntryFactory(htmlStream);
	}
	
	public DictionaryEntry searchByWord(String word) {
		DictionaryEntry dicEntry = null;
		Cursor c = _con.rawQuery("select def from word_db where word = ? limit 1", new String[]{word});
		
		if(0 < c.getCount()){
			c.moveToNext();
			dicEntry =  _factory.makeHTMLifiedEntry(word, c.getString(0));
		}
  		
		c.close();
		return dicEntry;
	}
	
	public DictionaryEntry getWordByID(long id) {
		DictionaryEntry dicEntry = null;
		Cursor c = _con.rawQuery("select word, def from word_db where _id = ? ", new String[]{new Long(id).toString()});
		
		if(0 < c.getCount()){
			c.moveToNext();
			dicEntry = _factory.makeHTMLifiedEntry(c.getString(0), c.getString(1));
		}
		
		c.close();
		return dicEntry;
	}
	
	public Vector<String> findMatchingHeadwords(String str) {
		Vector<String> headwords = new Vector<String>();
		Cursor c = _con.rawQuery("select word from word_db where word LIKE ? || '%' limit 50", new String[]{str});
		
		for(int i = 0; i < c.getCount(); i++){
			c.moveToNext();
			headwords.add(c.getString(0));
		}
  		
		c.close();
		return headwords;
	}
}
