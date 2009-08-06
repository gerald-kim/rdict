package com.amplio.rdict.search;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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

	public void atestFindExistingWordIndex() {
		assertEquals( 8, m_dictionary.findWordIndex( "abacus" ) );
		assertEquals( 21, m_dictionary.findWordIndex( "ABC" ) );

		assertEquals( 0, m_dictionary.findWordIndex( "a" ) );
		assertEquals( 1, m_dictionary.findWordIndex( "A" ) );
		assertEquals( 2, m_dictionary.findWordIndex( "a-" ) );
		assertEquals( 27, m_dictionary.findWordIndex( "abet" ) );
	}

	public void testFindNonExistingWordIndex() {
		assertEquals( 7, m_dictionary.findWordIndex( "ab" ) );
		assertEquals( 7, m_dictionary.findWordIndex( "aback" ) );
//		assertEquals( 7, m_dictionary.findWordIndex( "abackt" ) );
	}

	public void testGetDefinition() {
		DictionaryEntry e = m_dictionary.searchByWord( "a" );
		assertNotNull( e );
		// System.out.println( e.contents );
	}

	public void testComparator() {
		String[] strs = { "A", "a", "b", "bc", "a-", "ABC", "a.m." };
		Arrays.sort( strs, Dictionary.COLLATOR );
		for( int i = 0; i < strs.length; i++ ) {
			System.out.print( strs[i] );
			System.out.print( ", " );
		}

		assertTrue( Dictionary.COLLATOR.compare( "a", "a" ) == 0 );
		assertTrue( Dictionary.COLLATOR.compare( "a", "A" ) < 0 );
		assertTrue( Dictionary.COLLATOR.compare( "A", "a" ) > 0 );
		assertTrue( Dictionary.COLLATOR.compare( "A", "a-" ) < 0 );
	}
}
