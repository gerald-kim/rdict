package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;

import com.amplio.rdict.DictionaryEntryFactory.DictionaryEntry;
import com.strangegizmo.cdb.ModdedCdb;

public class Dictionary {
	private DictionaryEntryFactory _factory = null;
	
	private String[] _assetPaths = null;
	private AssetInputStreamProvider _aisp = null;
	
	public Dictionary(InputStream htmlStream, String[] assetPaths, AssetInputStreamProvider aisp) {
		_factory = new DictionaryEntryFactory(htmlStream);
		
		_assetPaths = assetPaths;
		_aisp = aisp;
	}
	
	public DictionaryEntry searchByWord(String word) {
		String def = null;
		
		ModdedCdb db = null;
		
  		for(int i = 0; i < _assetPaths.length;i++) {
  			// asset paths appear like assets/art
  			int wordIndex = _assetPaths[i].indexOf("_word");
  			
  			if(-1 != wordIndex) {
  				if(0 >= word.compareTo(_assetPaths[i].substring(_assetPaths[i].indexOf('/') + 1, wordIndex))){
	      			try {
		  				db = new ModdedCdb(_aisp.getAssetInputStream(_assetPaths[i]));
		  			} catch (IOException e) {
		  				e.printStackTrace();
		  			}
		  			
		  			byte[] bytes = db.find(word.getBytes());
		  		
		  			db.close();
		  			
		  			if (bytes != null) {
		  				def = new String(bytes);
		  				break;
		  			}
	      		}
  			}
      	}	
  		
      	if (def != null)
  			return _factory.makeHTMLifiedEntry(word, def);
  		else
  			return null;
	}

}
