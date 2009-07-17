package com.amplio.rdict;

public class DictionaryEntry {
	
	public long id = -1;
	public String headword = null;
	public String contents = null;
	
	public DictionaryEntry(String headword, String contents) {
		this.id = -1;
		this.headword = headword;
		this.contents = contents;
	}
	
	public DictionaryEntry(long id, String headword, String contents){
		this.id = id;
		this.headword = headword;
		this.contents = contents;
	}
}
