package com.amplio.rdict.history;

import com.amplio.rdict.history.History;
import com.amplio.rdict.search.DictionaryEntry;

import junit.framework.TestCase;

public class HistoryTest extends TestCase {

	public void testCanGoBack() {
		History h = new History();
		h.addWord(new DictionaryEntry(1, "word", "def"));
		
		assertTrue(! h.canGoBack());
		
		h.addWord(new DictionaryEntry(2, "word2", "def2"));
		
		assertTrue(h.canGoBack());
	}
	
	public void testCanGoForward() {
		History h = new History();
		h.addWord(new DictionaryEntry(1, "word", "def"));
		
		assertTrue(! h.canGoForward());
		
		h.addWord(new DictionaryEntry(2, "word2", "def2"));
		
		assertTrue(! h.canGoForward());
	}

	public void testGoForward() {
		History h = new History();
		h.addWord(new DictionaryEntry(1, "word", "def"));
		h.addWord(new DictionaryEntry(2, "word2", "def2"));
		
		h.goForward();
		
		assertTrue(! h.canGoForward());
		assertTrue( h.canGoBack());
	}
	
	public void testGoBack() {
		History h = new History();
		
		h.addWord(new DictionaryEntry(1, "word", "def"));
		h.addWord(new DictionaryEntry(2, "word2", "def2"));
		h.addWord(new DictionaryEntry(3, "word3", "def3"));		

		h.goBack();
		h.goBack();
		
		h.goForward();
		h.goForward();
		
		assertTrue(h.canGoBack());
		assertTrue(! h.canGoForward());
	}
	
	public void testGetWord() {
		History h = new History();
		
		h.addWord(new DictionaryEntry(1, "word", "def"));
		h.addWord(new DictionaryEntry(2, "word2", "def2"));
		h.addWord(new DictionaryEntry(3, "word3", "def3"));		

		assertEquals(3, h.getWord().id);
	}
	
	public void testIsEmpty() {
		History h = new History();
		
		assertTrue(h.isEmpty());
		
		h.addWord(new DictionaryEntry(1, "word", "def"));
		
		assertTrue(! h.isEmpty());
	}
	
	public void testClear() {
		History h = new History();
		h.addWord(new DictionaryEntry(1, "word", "def"));
		
		assertTrue(! h.isEmpty());
		
		h.clear();
		
		assertTrue(h.isEmpty());
	}
	
	public void testGoBackAndAddingErasesDownstreamItems() {
		History h = new History();
		
		h.addWord(new DictionaryEntry(1, "word", "def"));
		h.addWord(new DictionaryEntry(2, "word2", "def2"));
		h.addWord(new DictionaryEntry(3, "word3", "def3"));		

		h.goBack();
		h.goBack();
		
		h.addWord(new DictionaryEntry(4, "word4", "def4"));
		
		assertEquals(4, h.getWord().id);
		assertTrue(h.canGoBack());
		assertTrue(! h.canGoForward());
		assertEquals(2, h.size());
	}
	
}
