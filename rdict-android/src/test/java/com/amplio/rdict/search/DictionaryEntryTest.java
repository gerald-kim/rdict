package com.amplio.rdict.search;

import com.amplio.rdict.search.DictionaryEntry;

import junit.framework.TestCase;

public class DictionaryEntryTest extends TestCase {
	
	public void testConstructor() {
		String headword = "fish";
		String contents = "this is the contents of the entry for the headword fish";
		
		DictionaryEntry hw = new DictionaryEntry(headword, contents);
		
		assertEquals(-1, hw.id);
		assertEquals(headword, hw.headword);
		assertEquals(contents, hw.contents);
	}
	
	public void testConstructorForFetchFromDB() {
		long id = 1;
		String headword = "fish";
		String contents = "this is the contents of the entry for the headword fish";
		
		DictionaryEntry hw = new DictionaryEntry(id, headword, contents);
		
		assertEquals(id, hw.id);
		assertEquals(headword, hw.headword);
		assertEquals(contents, hw.contents);
	}
	
}
