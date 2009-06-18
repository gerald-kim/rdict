package com.amplio.rdict.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import junit.framework.TestCase;

import com.amplio.rdict.AssetInputStreamProvider;
import com.amplio.rdict.Dictionary;
import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;

public class DictionaryTest extends TestCase implements AssetInputStreamProvider{
	
	private InputStream _htmlStream = null;
	
	String[] _assetPaths = null;
	
	public void setUp() {
		// set up search results page stream
		File file = new File("assets/dictionary_js.html");
		try {
			_htmlStream = (InputStream) new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		File dir = new File("assets");
	    _assetPaths = dir.list();
	    
	    for(int i = 1; i < _assetPaths.length;i++) {
	    	if(-1 != _assetPaths[i].indexOf("word")){
	    		_assetPaths[i] = "assets/" + _assetPaths[i];
	    	}
	    }
	}
	
	public void testSearch() {
		Dictionary dic = new Dictionary(_htmlStream, _assetPaths, this);
		
		DictionaryEntry dicEntry = dic.searchByWord("fish");
		
		assertEquals("fish", dicEntry.word);
		assertTrue(-1 != dicEntry.entry.indexOf("</script>"));
		
		dicEntry = dic.searchByWord("water");
		assertEquals("water", dicEntry.word);
	}
	
	public void testSearchReturnsNullIfNotFound() {
		Dictionary dic = new Dictionary(_htmlStream, _assetPaths, this);
		
		DictionaryEntry dicEntry = dic.searchByWord("dfasdfasdf");
		
		assertEquals(null, dicEntry);
	}
	
	public void testGetDirContents() {
		File dir = new File("assets");
	    
	    String[] children = dir.list();
	    
	    assertTrue( 0 < children.length);
	}

	public InputStream getAssetInputStream(String path) {
		InputStream is = null;
		try {
			File file = new File(path);
			is = (InputStream) new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return is;
	}
}
