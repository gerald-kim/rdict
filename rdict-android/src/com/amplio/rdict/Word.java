package com.amplio.rdict;

public class Word {
	
	public long _id = 0;
	public String _word = null;
	public String _def = null;
	
	public Word(long id, String word, String def){
		_id = id;
		_word = word;
		_def = def;
	}

}
