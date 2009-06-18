package com.amplio.rdict.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import junit.framework.TestCase;

import com.amplio.rdict.DictionaryEntryFactory;
import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;

public class DictionaryEntryFactoryTest extends TestCase {

	public void testInit() {
		DictionaryEntry dicEntry = DictionaryEntryFactory.makeEntry("fish", "The entry appears here in HTML.");
		
		assertEquals("fish", dicEntry.word);
		assertEquals("The entry appears here in HTML.", dicEntry.entry);
	}

	public void testMakeHTMLifiedEntry() {
		File file = new File("assets/dictionary_js.html");
		InputStream htmlStream = null;
		try {
			htmlStream = (InputStream) new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
		DictionaryEntry dicEntry = DictionaryEntryFactory.makeHTMLifiedEntry(htmlStream, "fish", "The entry appears here in HTML.");
		
		System.out.println(dicEntry.entry);
		
		assertTrue(-1 != dicEntry.entry.indexOf("script"));
	}
	
}
