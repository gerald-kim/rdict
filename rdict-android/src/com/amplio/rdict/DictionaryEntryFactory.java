package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;

public class DictionaryEntryFactory {
	
	public static class DictionaryEntry {
		public String word = null;
		public String  entry = null;
		
		public DictionaryEntry(String key, String value){
			word = key;
			entry = value;
		}
	}
	
	private String _htmlFileContents = null;
	
	public DictionaryEntryFactory(String htmlFileContents){
		_htmlFileContents = htmlFileContents;
	}
		
	public static DictionaryEntry makeEntry(String word, String def) {
		return new DictionaryEntry(word, def);
	}

	public static DictionaryEntry makeHTMLifiedEntry(InputStream htmlStream, String word, String def){
		StringBuffer sb = new StringBuffer();
		
		byte[] buffer = null;
		try {
			int size = htmlStream.available();
			buffer = new byte[size];
			
			htmlStream.read(buffer);
			htmlStream.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
        }

		sb.append(new String(buffer));
		sb.append(def);
		
		return new DictionaryEntry(word, sb.toString());
	}





	

}
