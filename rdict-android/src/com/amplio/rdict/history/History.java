package com.amplio.rdict.history;

import java.util.Vector;

import com.amplio.rdict.search.DictionaryEntry;

public class History {

	int _index = 0;
	Vector<DictionaryEntry> words = new Vector<DictionaryEntry>();
	
	public void addWord(DictionaryEntry word) {
		
		if(_index < words.size()) {
			words.setSize(_index + 1);
		}
		
		words.add(word);
		_index = words.size() - 1;
	}
	
	public boolean canGoBack() {
		return _index > 0;
	}

	public boolean canGoForward() {
		return _index < words.size() - 1;
	}

	public void goForward() {
		_index++;
	}

	public void goBack() {
		_index--;
	}

	public DictionaryEntry getWord() {
		return words.elementAt(_index);
	}

	public boolean isEmpty() {
		return 0 == words.size();
	}

	public void clear() {
		words.removeAllElements();
	}

	public int size() {
		return words.size();
	}
}
