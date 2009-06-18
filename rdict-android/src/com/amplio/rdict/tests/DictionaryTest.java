package com.amplio.rdict.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import junit.framework.TestCase;

import com.amplio.rdict.Dictionary;
import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;

public class DictionaryTest extends TestCase {
	
	private InputStream _htmlStream = null;
	private Vector _assetInputstreams = null;
	
	public void setUp() {
		// set up search results page stream
		File file = new File("assets/dictionary_js.html");
		try {
			_htmlStream = (InputStream) new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// set up dictionary db streams
		_assetInputstreams = new Vector();
		File dir = new File("assets");
	    String[] assetPaths = dir.list();
	    
	    for(int i = 1; i < assetPaths.length;i++) {
	    	if(-1 != assetPaths[i].indexOf("word")){
	    		try {
	    			_assetInputstreams.add((InputStream) new FileInputStream("assets/" + assetPaths[i]));
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }
	}
	
	public void testSearch() {
		Dictionary dic = new Dictionary(_htmlStream, _assetInputstreams);
		
		DictionaryEntry dicEntry = dic.searchByWord("fish");
		
		assertEquals("fish", dicEntry.word);
		assertTrue(-1 != dicEntry.entry.indexOf("</script>"));
	}
	
	public void testGetDirContents() {
		File dir = new File("assets");
	    
	    String[] children = dir.list();
	    
	    assertTrue( 0 < children.length);
	}
}
