package com.amplio.rdict.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.amplio.rdict.search.AssetInputStreamProvider;
import com.amplio.rdict.search.Dictionary;
import com.amplio.rdict.search.DictionaryEntry;

public class DictionaryTest extends AndroidTestCase implements AssetInputStreamProvider{
	
	private InputStream _htmlStream = null;
	
	private SQLiteDatabase con = null;
	
	public void setUp() {
		// set up search results page stream
		File file = new File("assets/dictionary_js.html");
		
		try {
			_htmlStream = (InputStream) new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void tearDown() {
		con.close();
	}
	
	public void testSearch() {
		Dictionary dic = new Dictionary(con, _htmlStream);
		
		DictionaryEntry dicEntry = dic.searchByWord("fish");
		
		assertEquals("fish", dicEntry.headword);
		assertTrue(-1 != dicEntry.contents.indexOf("</script>"));
		
		dicEntry = dic.searchByWord("water");
		assertEquals("water", dicEntry.headword);
	}
	
	public void testSearchReturnsNullIfNotFound() {
		Dictionary dic = new Dictionary(con, _htmlStream);
		
		DictionaryEntry dicEntry = dic.searchByWord("dfasdfasdf");
		
		assertEquals(null, dicEntry);
	}
	
	public void testStringComparison() {
		assertTrue(0 > "apple".compareTo("fish"));
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