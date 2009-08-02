package com.amplio.rdict.history;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amplio.rdict.search.DictionaryEntry;

public class HistoryManager {
	public final static int HISTORY_LENGTH_IN_WORDS = 50;
	
	private SQLiteDatabase _con = null;
	
	public HistoryManager(SQLiteDatabase con) {
		_con = con;
	}
	
	public void addHistoryRecord(String word){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		Cursor c = _con.rawQuery("select count(*) from history", null);
		c.moveToFirst();
		
		int count = c.getInt(0);
		
		c.close();
		
		if (count <= HISTORY_LENGTH_IN_WORDS) {
			_con.execSQL("insert into history values (null, ?, ?)", new String[]{word, sdf.format(cal.getTime())});
		}
		
		else {
			c = _con.rawQuery("select _id, date from history order by date asc", null);
			c.moveToFirst();
			
			for(int i = 0; i < count - HISTORY_LENGTH_IN_WORDS - 1; i++) {
				long idOfOldestRecord = c.getLong(0);
				
				_con.execSQL("delete from history where _id = ?", new String[]{new Long(idOfOldestRecord).toString()});
				
				c.moveToNext();
			}
			
			c.close();
			
			
			_con.execSQL("insert into history values (null, ?, ?)", new String[]{word, sdf.format(cal.getTime())});
		}		
	}
	
	public Vector<DictionaryEntry> loadHistoryRecordsByDate(String yyyyMMdd) {
		Vector<DictionaryEntry> words = new Vector<DictionaryEntry>();
		
		Cursor c = _con.rawQuery("select _id, word, date from history where date = ? order by _id DESC", new String[]{yyyyMMdd});
		
		for(int i = 0; i < c.getCount(); i++){
			c.moveToNext();
			words.add(new DictionaryEntry(c.getLong(0), c.getString(1), null));
		}
		
		c.close();
		return words;
	}

	public void createTableIfNotExists(SQLiteDatabase con) {
		_con.execSQL(" create table if not exists history (_id integer primary key, word text, date text) ");
	}
	
}
