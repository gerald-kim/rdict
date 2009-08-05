package com.amplio.rdict.search;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public class DictionaryTest extends TestCase {
	private Dictionary m_dictionary;

	public void setUp() {
		InputStream is = null;
		try {
			is = new FileInputStream( "assets/dictionary_js.html" );
			m_dictionary = new Dictionary( "src/test/resources/word.cdb",
			        "src/test/resources/word.index", is );
			is.close();
		} catch( FileNotFoundException e ) {
			e.printStackTrace();
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

	public void tearDown() {
		// m_dictionary.
	}

	public void testInit() {
		assertEquals( 1522, m_dictionary.words.length );
	}

	public void testFindWordIndex() {
		assertEquals( 6, m_dictionary.findWordIndex( "aaaa" ) );
		assertEquals( 0, m_dictionary.findWordIndex( "a" ) );
		assertEquals( 1, m_dictionary.findWordIndex( "A" ) );
		assertEquals( 2, m_dictionary.findWordIndex( "a-" ) );
		assertEquals( 6, m_dictionary.findWordIndex( "aaa" ) );
		assertEquals( 0, m_dictionary.findWordIndex( "" ) );
	}

	public void testGetDefinition() {
		DictionaryEntry e = m_dictionary.searchByWord( "a" );
		assertNotNull( e );
//		System.out.println( e.contents );
	}
}
