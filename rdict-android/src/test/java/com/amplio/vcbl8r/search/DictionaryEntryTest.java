package com.amplio.vcbl8r.search;

import com.amplio.vcbl8r.search.DictionaryEntry;

import junit.framework.TestCase;

public class DictionaryEntryTest extends TestCase {
	
	public void testConstructor() {
		String headword = "fish";
		String contents = "this is the contents of the entry for the headword fish";
		
		DictionaryEntry hw = new DictionaryEntry(headword, contents);
		
		assertEquals(headword, hw.headword);
		assertEquals(contents, hw.contents);
	}
	
	public void testConstructorForFetchFromDB() {
		String headword = "fish";
		String contents = "this is the contents of the entry for the headword fish";
		
		DictionaryEntry hw = new DictionaryEntry(headword, contents);
		
		assertEquals(headword, hw.headword);
		assertEquals(contents, hw.contents);
	}
	
}
