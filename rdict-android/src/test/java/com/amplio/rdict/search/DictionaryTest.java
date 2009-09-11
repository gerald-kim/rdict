package com.amplio.rdict.search;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.RuleBasedCollator;

import junit.framework.TestCase;

public class DictionaryTest extends TestCase {
	private Dictionary dictionary;

	public void setUp() {
		InputStream is = null;
		try {
			is = new FileInputStream( "assets/dictionary_js.html" );
			dictionary = new Dictionary();
			dictionary.load("src/test/resources/word.cdb",
			        		"src/test/resources/word.index", is, null, null );
			is.close();
		} catch( FileNotFoundException e ) {
			e.printStackTrace();
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

	public void tearDown() {
		// dictionary.
	}

	public void testInit() {
		assertEquals( 28134, Dictionary.words.length );
	}

	public void testFindExistingWordIndex() {
		assertEquals( 0, dictionary.findWordIndex( "'em" ) );
		assertEquals( 3, dictionary.findWordIndex( "a" ) );
		assertEquals( 4, dictionary.findWordIndex( "A" ) );
		assertEquals( 5, dictionary.findWordIndex( "a-" ) );
		assertEquals( 31, dictionary.findWordIndex( "abet" ) );
		assertEquals( 456, dictionary.findWordIndex( "age" ) );
		assertEquals( 457, dictionary.findWordIndex( "-age" ) );
		assertEquals( 8048, dictionary.findWordIndex( "er" ) );
		assertEquals( 8049, dictionary.findWordIndex( "ER" ) );
		assertEquals( 8050, dictionary.findWordIndex( "-er" ) );
	}
	
	public void testFindExistingWordIndexIfWordWouldAppearAfterLastInList() {
		assertEquals(Dictionary.wordsLoaded - 1, dictionary.findWordIndex("zzzzzzzzzzzz") );
	}

	public void testFindNonExistingWordIndex() {
		assertEquals( 10, dictionary.findWordIndex( "ab" ) );
		assertEquals( 10, dictionary.findWordIndex( "aback" ) );
		assertEquals( 10, dictionary.findWordIndex( "abackt" ) );
	}

	public void testGetDefinition() {
		DictionaryEntry e = dictionary.searchByWord( "a" );
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
