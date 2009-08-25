package com.amplio.rdict.history;

import java.util.Vector;

import com.amplio.rdict.search.DictionaryEntry;

public class History {

	int m_index = 0;
	Vector<DictionaryEntry> m_words = new Vector<DictionaryEntry>();
	
	public void addWord(DictionaryEntry word) {
		if(m_index < m_words.size()) {
			m_words.setSize(m_index + 1);
		}
		
		m_words.add(word);
		m_index = m_words.size() - 1;
	}
	
	public boolean canGoBack() {
		return m_index > 0;
	}

	public boolean canGoForward() {
		return m_index < m_words.size() - 1;
	}

	public void goForward() {
		m_index++;
	}

	public void goBack() {
		int prevIndex = m_index;
		
		if(null == this.m_words.get(prevIndex))
			this.m_words.remove(prevIndex);
		
		m_index--;
	}

	public DictionaryEntry getWord() {
		return m_words.elementAt(m_index);
	}

	public boolean isEmpty() {
		return 0 == m_words.size();
	}

	public void clear() {
		m_words.removeAllElements();
	}

	public int size() {
		return m_words.size();
	}

	public boolean containsEntryWithHeadword( String headword ) {
		for(DictionaryEntry e : m_words)
			if(headword.equals(e.headword))
				return true;
	    
		return false;
    }
}
