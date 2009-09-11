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

import android.os.Handler;

import com.amplio.rdict.LoadDictionaryService;
import com.strangegizmo.cdb.Cdb;

public class Dictionary {
	public static RuleBasedCollator COLLATOR  = (RuleBasedCollator) Collator.getInstance(new Locale("en", "US", ""));

	private DictionaryEntryFactory factory = null;
	private Cdb wordCdb;
	public static String[] words;

	public static int wordsLoaded = 0;

	public Dictionary() {
	}

	public void load( String wordDbPath, String wordIndexPath, InputStream htmlStream ) {
		Dictionary.wordsLoaded = 0;

		factory = new DictionaryEntryFactory( htmlStream );
		try {
			wordCdb = new Cdb( wordDbPath );

			BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(
			        wordIndexPath ) ) );

			int wordCount = Integer.parseInt( reader.readLine() );

			words = new String[wordCount];

			for( int i = 0; i < wordCount; i++ ) {
				words[i] = reader.readLine();

				Dictionary.wordsLoaded++;
			}

			Arrays.sort( words, COLLATOR );

			reader.close();
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

	public void load( String wordDbPath, String wordIndexPath, InputStream htmlStream,
	        Handler loadProgressHandler, Runnable updateRunnable ) {
		Dictionary.wordsLoaded = 0;

		factory = new DictionaryEntryFactory( htmlStream );
		try {
			wordCdb = new Cdb( wordDbPath );

			BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(
			        wordIndexPath ) ) );

			int wordCount = Integer.parseInt( reader.readLine() );

			words = new String[wordCount];

			for( int i = 0; i < wordCount; i++ ) {
				words[i] = reader.readLine();

				Dictionary.wordsLoaded++;
				
				if(loadProgressHandler != null) {
					if(Dictionary.wordsLoaded % 5 == 0) {
						this.postProgress();
						
						if(shouldQuitLoad()) {
							return;
						}
					}
				}
			}

			reader.close();
		} catch( IOException e ) {
			e.printStackTrace();
		}
		
		if(loadProgressHandler != null) {
			this.postProgress();
		}
	}

	private boolean shouldQuitLoad() {
		return !LoadDictionaryService.isRunning;
	}

	public void postProgress() {
		if( LoadDictionaryService.splashActivity != null ) {
			Runnable runnable = LoadDictionaryService.splashActivity.getRunnableForDBInit();
			LoadDictionaryService.splashActivity.getHandler().post( runnable );
		}
	}

	public Handler getHandler() {
		return LoadDictionaryService.splashActivity.getHandler();
	}

	public Runnable getRunnable() {
		return LoadDictionaryService.splashActivity.getRunnableForDBInit();
	}

	public DictionaryEntry searchByWord( String word ) {
		DictionaryEntry dicEntry = factory.makeHTMLifiedEntry( word, 
															new String( wordCdb.find( word.getBytes() ) ) );

		return dicEntry;
	}

	public int findWordIndex( String word ) {
		int wordIndex = 0;
		int idx = 0;
		int prevMatchingIdx = 0;
		while( wordIndex < word.length()) {
			wordIndex++;
			String wordSubstring = word.substring( 0, wordIndex );
			idx = Arrays.binarySearch( words, wordSubstring, COLLATOR );
//			System.out.println( "word: '" + wordSubstring + "', wordIndex: " + wordIndex
//			        + ", idx: " + idx + ", prevIdx: " + prevMatchingIdx );

			if( idx < 0 ) {
				if( -idx - 1 >= words.length - 1 ) {
					return words.length - 1;
					// } else if( !words[-idx].toLowerCase().startsWith(
					// wordSubstring.toLowerCase() ) ) {
					// System.out.println( "words[-" + -idx + "]: " +
					// words[-idx] );
					// break;
				}
			}
			if ( idx >= 0 ) {
				prevMatchingIdx = idx;
			}

		}
//		System.out.println( "pass" );

		if( idx < 0 && prevMatchingIdx >= 0 ) {
			if ( words[-idx-1].toLowerCase().startsWith( word ) ) {
				idx = -idx -1;
			} else {
//			System.out.println( "replacing with prevIndex" );
				idx = prevMatchingIdx;
			}
		}
		return idx;
	}

	public static int getProgress() {
		return (Dictionary.wordsLoaded * 100) / words.length;
	}
}
