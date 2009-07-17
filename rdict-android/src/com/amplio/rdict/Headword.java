package com.amplio.rdict;

public class Headword {
	
	public long id = 0;
	public String headword = null;
	public String contents = null;
	
	public Headword(long id, String headword, String contents){
		this.id = id;
		this.headword = headword;
		this.contents = contents;
	}

}
