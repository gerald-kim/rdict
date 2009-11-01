package com.amplio.vcbl8r.history;

import java.util.Vector;

import com.amplio.vcbl8r.search.DictionaryEntry;

public class History {

	int index = 0;
	Vector<DictionaryEntry> words = new Vector<DictionaryEntry>();
	
	public void addWord(DictionaryEntry word) {
		if(index < words.size()) {
			words.setSize(index + 1);
		}
		
		words.add(word);
		index = words.size() - 1;
	}
	
	public boolean canGoBack() {
		return index > 0;
	}

	public boolean canGoForward() {
		return index < words.size() - 1;
	}

	public void goForward() {
		index++;
	}

	public void goBack() {
		int prevIndex = index;
		
		if(null == this.words.get(prevIndex))
			this.words.remove(prevIndex);
		
		index--;
	}

	public DictionaryEntry getWord() {
		return words.elementAt(index);
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

	public boolean containsEntryWithHeadword( String headword ) {
		for(DictionaryEntry e : words)
			if(headword.equals(e.headword))
				return true;
	    
		return false;
    }
}
