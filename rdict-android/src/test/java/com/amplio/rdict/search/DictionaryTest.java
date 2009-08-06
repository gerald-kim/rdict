package com.amplio.rdict.search;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.RuleBasedCollator;

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
		assertEquals( 28134, m_dictionary.words.length );
	}

	public void atestFindExistingWordIndex() {
		assertEquals( 0, m_dictionary.findWordIndex( "'em" ) );
		assertEquals( 3, m_dictionary.findWordIndex( "a" ) );
		assertEquals( 4, m_dictionary.findWordIndex( "A" ) );
		assertEquals( 5, m_dictionary.findWordIndex( "a-" ) );
		assertEquals( 31, m_dictionary.findWordIndex( "abet" ) );
		assertEquals( 8048, m_dictionary.findWordIndex( "er" ) );
		assertEquals( 8049, m_dictionary.findWordIndex( "ER" ) );
		assertEquals( 8050, m_dictionary.findWordIndex( "-er" ) );
	}

	public void testFindNonExistingWordIndex() {
		assertEquals( 10, m_dictionary.findWordIndex( "ab" ) );
		assertEquals( 10, m_dictionary.findWordIndex( "aback" ) );
		assertEquals( 11, m_dictionary.findWordIndex( "abackt" ) );
	}

	public void testGetDefinition() {
		DictionaryEntry e = m_dictionary.searchByWord( "a" );
		assertNotNull( e );
		// System.out.println( e.contents );
	}

	public void testComparator() throws ParseException {
		RuleBasedCollator collator =  Dictionary.COLLATOR;
		//System.out.println( collator.getRules() );
		
//		String[] strs = { "'em", "A", "a", "b", "ar", "am", "-am", "bc", "a-", "ABC", "a.m." };
//		Arrays.sort( strs, Dictionary.COLLATOR );
//		for( int i = 0; i < strs.length; i++ ) {
//			System.out.print( strs[i] );
//			System.out.print( ", " );
//		}

		assertTrue( collator.compare( "'", "a" ) < 0 );
		assertTrue( collator.compare( "-", "a" ) < 0 );
		assertTrue( collator.compare( "a", "a" ) == 0 );
		assertTrue( collator.compare( "a", "A" ) < 0 );
		assertTrue( collator.compare( "A", "a" ) > 0 );
		assertTrue( collator.compare( "A", "a-" ) < 0 );
	}
}
