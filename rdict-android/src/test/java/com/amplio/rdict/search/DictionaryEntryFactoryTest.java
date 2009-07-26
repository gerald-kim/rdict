package com.amplio.rdict.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import junit.framework.TestCase;

import com.amplio.rdict.search.DictionaryEntry;
import com.amplio.rdict.search.DictionaryEntryFactory;

public class DictionaryEntryFactoryTest extends TestCase {

	private InputStream _htmlStream = null;
	
	public void setUp() {
		File file = new File("assets/dictionary_js.html");
		
		try {
			_htmlStream = (InputStream) new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void testInit() {
		DictionaryEntryFactory factory = new DictionaryEntryFactory(_htmlStream);
		DictionaryEntry dicEntry = factory.makeEntry("fish", "The entry appears here in HTML.");
		
		assertEquals("fish", dicEntry.headword);
		assertEquals("The entry appears here in HTML.", dicEntry.contents);
	}

	public void testMakeHTMLifiedEntry() {
		DictionaryEntryFactory factory = new DictionaryEntryFactory(_htmlStream);
		DictionaryEntry dicEntry = factory.makeHTMLifiedEntry("fish", "The entry appears here in HTML.");
		
		assertTrue(-1 != dicEntry.contents.indexOf("script"));
	}
}
