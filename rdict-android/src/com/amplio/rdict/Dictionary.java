package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;
import com.strangegizmo.cdb.ModdedCdb;

public class Dictionary {

	private InputStream _htmlStream = null;
	private Vector _searchFiles = null;
	
	public Dictionary(InputStream htmlStream, Vector searchFiles) {
		_htmlStream = htmlStream;
		_searchFiles = searchFiles;
	}
	
	public DictionaryEntry searchByWord(String word) {
		DictionaryEntry entry = null;
		
		String def = null;
		
		ModdedCdb db = null;
  		
      	for(int i = 0; i < _searchFiles.size();i++) {	
  	    
      		try {
  				db = new ModdedCdb((InputStream) _searchFiles.elementAt(i));
  			} catch (IOException e) {
  				e.printStackTrace();
  			}
  			
  			byte[] bytes = db.find(word.getBytes());
  		
  			if (bytes != null) {
  				def = new String(bytes);
  				break;
  			}
      	}	
  				
  		db.close();
		
		return DictionaryEntryFactory.makeHTMLifiedEntry(_htmlStream, word, def);
	}

}
