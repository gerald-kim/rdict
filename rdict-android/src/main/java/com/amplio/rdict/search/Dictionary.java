package com.amplio.rdict.search;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

import com.strangegizmo.cdb.Cdb;

public class Dictionary {
	private Comparator<String> c = new Comparator<String>() {
		public int compare( String str1, String str2 ) {
			int compareTo = str1.toLowerCase().compareTo( str2.toLowerCase() );
			// System.out.println( "comparing :" + str1 + ", " + str2 + " = " +
			// compareTo);
			return compareTo;
		}
	};

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
		int i = _find( word );
		int wordIdx = word.length();

		while( i < 0 && wordIdx > 1) {
			wordIdx--;
			i = _find( word.substring( 0, wordIdx ) );
			System.out.println( "research i" + i );
		}
		if( i < 0 ) {
			i = 0;	
		}

		return i;
	}

	private int _find( String word ) {
		int i = Arrays.binarySearch( words, word, c );

		if( i < 0 || words[i].equals( word ) ) {
			return i;
		}

		while( true) {
			if( i == 0 )
				break;

			System.out.println( "idx: " + i );
			System.out.println( "comparing :" + words[i - 1] + ", " + word + ": "
			        + c.compare( words[i - 1], word ) );
			if( c.compare( words[i - 1], word ) < 0 ) {
				i++;
				break;
			}

			i--;
		}
		return i;
	}
}
