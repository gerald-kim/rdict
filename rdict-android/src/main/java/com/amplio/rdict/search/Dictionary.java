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
	public static RuleBasedCollator COLLATOR  = (RuleBasedCollator)
    Collator.getInstance(new Locale("en", "US", ""));

	private DictionaryEntryFactory factory = null;
	private Cdb wordCdb;
	public static String[] words;

	public static int wordsLoaded = 0;
	
	public Dictionary( String wordDbPath, 
						String wordIndexPath, 
						InputStream htmlStream, 
						Handler loadProgressHandler,
						Runnable updateRunnable) {
		
		Dictionary.wordsLoaded = 0;
		
		factory = new DictionaryEntryFactory( htmlStream );
		try {
			wordCdb = new Cdb( wordDbPath );

			BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(
			        									wordIndexPath ) ) );
			
			int wordCount = Integer.parseInt(reader.readLine());
			
			words = new String[wordCount];
			
			for( int i = 0; i < wordCount; i++ ) {
				words[i] = reader.readLine();
				
				Dictionary.wordsLoaded++;
				
				if(Dictionary.wordsLoaded % 5 == 0 && loadProgressHandler != null)
					this.postProgress();
			}

			reader.close();
		} catch( IOException e ) {
			e.printStackTrace();
		}

		this.postProgress();
	}
	
	public void postProgress() {
		if(LoadDictionaryService.splashActivity != null) {
			Runnable runnable = LoadDictionaryService.splashActivity.getRunnableForDBInit();
			LoadDictionaryService.splashActivity.getHandler().post(runnable);
		}
	}
	
	public Handler getHandler() {
		return LoadDictionaryService.splashActivity.getHandler();
	}
	
	public Runnable getRunnable() {
		return LoadDictionaryService.splashActivity.getRunnableForDBInit();
	}
	

	public DictionaryEntry searchByWord( String word ) {
		DictionaryEntry dicEntry = factory.makeHTMLifiedEntry( word, new String( wordCdb
		        .find( word.getBytes() ) ) );

		return dicEntry;
	}

	public int findWordIndex( String word ) {
		int wordIndex = 0;
		int idx = 0;
		while( wordIndex < word.length()) {
			wordIndex++;
			idx = Arrays.binarySearch( words, word.substring( 0, wordIndex ), Dictionary.COLLATOR );
			
			if( idx < 0 ) {
				if ( -idx - 1 >= words.length - 1) return words.length - 1;
				else if( !words[-idx].toLowerCase().startsWith(
				        word.substring( 0, wordIndex ).toLowerCase() ) )
					break;
			}
			// System.out.println( "wordIndex: " + wordIndex + ", idx: " + idx
			// );
		}

		if( idx < 0 )
			idx = -idx - 1;
		return idx;
	}

	public static int getProgress() {
		return (Dictionary.wordsLoaded*100) / words.length;
	}
}
