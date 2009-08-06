package com.amplio.rdict.search;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Arrays;
import java.util.Locale;

import com.strangegizmo.cdb.Cdb;

public class Dictionary {
	public static final RuleBasedCollator COLLATOR = (RuleBasedCollator) Collator.getInstance( new Locale(
	        "en", "US", "" ) );

	private DictionaryEntryFactory m_factory = null;
	private Cdb m_wordCdb;
	public String[] words;

	public Dictionary( String wordDbPath, String wordIndexPath, InputStream htmlStream ) {
		m_factory = new DictionaryEntryFactory( htmlStream );
		try {
			m_wordCdb = new Cdb( wordDbPath );

			BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(
			        wordIndexPath ) ) );
			int wordCount = Integer.parseInt( reader.readLine().trim() );
			words = new String[wordCount];
			for( int i = 0; i < wordCount; i++ ) {
				words[i] = reader.readLine().trim();
			}
			reader.close();
		} catch( IOException e ) {
			e.printStackTrace();
		}

	}

	public DictionaryEntry searchByWord( String word ) {
		DictionaryEntry dicEntry = m_factory.makeHTMLifiedEntry( word, new String( m_wordCdb
		        .find( word.getBytes() ) ) );
		
		return dicEntry;
	}

	public int findWordIndex( String word ) {
		int wordIndex = 0; 
		int idx = 0;
		while( wordIndex < word.length() ) {
			wordIndex++;
			idx = Arrays.binarySearch( words, word.substring( 0, wordIndex  ), Dictionary.COLLATOR );
			if ( idx < 0  ) {
			 	if ( ! words[-idx].toLowerCase().startsWith( word.substring( 0, wordIndex ).toLowerCase() ) )
			 		break;
			}
 			System.out.println( "wordIndex: " + wordIndex + ", idx: " + idx );
		}
		
		if( idx < 0 ) 
			idx = -idx - 1;
		return idx;
	}
}
