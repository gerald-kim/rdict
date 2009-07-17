package com.amplio.rdict;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HistoryManager {
	private SQLiteDatabase _con = null;
	
	public HistoryManager(SQLiteDatabase con) {
		_con = con;
	}
	
	public void addHistoryRecord(String word){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		_con.execSQL("insert into history values (null, ?, ?)", new String[]{word, sdf.format(cal.getTime())});
	}
	
	public Vector<Headword> loadHistoryRecordsByDate(String yyyyMMdd) {
		Vector<Headword> words = new Vector<Headword>();
		
		Cursor c = _con.rawQuery("select _id, word, date from history where date = ? order by word ASC", new String[]{yyyyMMdd});
		
		for(int i = 0; i < c.getCount(); i++){
			c.moveToNext();
			words.add(new Headword(c.getLong(0), c.getString(1), null));
		}
		
		return words;
	}

	public void createTableIfNotExists(SQLiteDatabase con) {
		_con.execSQL(" create table if not exists history (_id integer primary key, word text, date text) ");
	}
	
}
