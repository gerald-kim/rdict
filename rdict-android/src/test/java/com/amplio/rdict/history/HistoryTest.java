package com.amplio.rdict.history;

import junit.framework.TestCase;

import com.amplio.rdict.search.DictionaryEntry;

public class HistoryTest extends TestCase {

	public void testCanGoBack() {
		History h = new History();
		h.addWord(new DictionaryEntry("word", "def"));
		
		assertTrue(! h.canGoBack());
		
		h.addWord(new DictionaryEntry("word2", "def2"));
		
		assertTrue(h.canGoBack());
	}
	
	public void testCanGoForward() {
		History h = new History();
		h.addWord(new DictionaryEntry("word", "def"));
		
		assertTrue(! h.canGoForward());
		
		h.addWord(new DictionaryEntry("word2", "def2"));
		
		assertTrue(! h.canGoForward());
	}
	
	public void testGoBack() {
		History h = new History();
		
		h.addWord(new DictionaryEntry("word", "def"));
		h.addWord(new DictionaryEntry("word2", "def2"));
		h.addWord(new DictionaryEntry("word3", "def3"));		

		h.goBack();
		h.goBack();
		
		h.goForward();
		h.goForward();
		
		assertTrue(h.canGoBack());
		assertTrue(! h.canGoForward());
	}

	public void testGoForward() {
		History h = new History();
		h.addWord(new DictionaryEntry("word", "def"));
		h.addWord(new DictionaryEntry("word2", "def2"));
		
		h.goForward();
		
		assertTrue(! h.canGoForward());
		assertTrue( h.canGoBack());
	}
	
	public void testGetWord() {
		History h = new History();
		
		h.addWord(new DictionaryEntry("word", "def"));
		h.addWord(new DictionaryEntry("word2", "def2"));
		h.addWord(new DictionaryEntry("word3", "def3"));		

		assertEquals("word3", h.getWord().headword );
		
	}
	
	public void testIsEmpty() {
		History h = new History();
		
		assertTrue(h.isEmpty());
		
		h.addWord(new DictionaryEntry("word", "def"));
		
		assertTrue(! h.isEmpty());
	}
	
	public void testClear() {
		History h = new History();
		h.addWord(new DictionaryEntry("word", "def"));
		
		assertTrue(! h.isEmpty());
		
		h.clear();
		
		assertTrue(h.isEmpty());
	}
	
	public void testGoBackAndAddingErasesDownstreamItems() {
		History h = new History();
		
		h.addWord(new DictionaryEntry("word", "def"));
		h.addWord(new DictionaryEntry("word2", "def2"));
		h.addWord(new DictionaryEntry("word3", "def3"));		

		h.goBack();
		h.goBack();
		
		h.addWord(new DictionaryEntry("word4", "def4"));
		
		assertEquals("word4", h.getWord().headword);
		assertTrue(h.canGoBack());
		assertTrue(! h.canGoForward());
		assertEquals(2, h.size());
	}
	
	public void testGoBackFromNullErasesNullEntry() {
		History h = new History();
		
		h.addWord(new DictionaryEntry("word2", "def2"));
		h.addWord(null);		
		
		h.goBack();
		
		assertEquals("word2", h.getWord().headword);
		assertTrue(! h.canGoForward());
		assertEquals(1, h.size());
	}
	
	public void testContainsEntryWithHeadword() {
		History h = new History();
		
		h.addWord(new DictionaryEntry("word", "def2"));
		h.addWord(new DictionaryEntry("word2", "def2"));		
		
		assertTrue(h.containsEntryWithHeadword("word"));
		assertTrue(h.containsEntryWithHeadword("word2"));
		assertTrue(! h.containsEntryWithHeadword("word9"));
	}
	
}
