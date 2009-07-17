package com.amplio.rdict;

import java.io.IOException;
import java.io.InputStream;

public class DictionaryEntryFactory {
	private String _htmlFileContents = null;
	
	public DictionaryEntryFactory(InputStream htmlStream){
		_htmlFileContents = loadHTMLFileContents(htmlStream);
	}
	
	public DictionaryEntry makeEntry(String word, String def) {
		return new DictionaryEntry(word, def);
	}

	public DictionaryEntry makeHTMLifiedEntry(String word, String def){
		StringBuffer sb = new StringBuffer();
		sb.append(_htmlFileContents);
		sb.append(def);
		
		return new DictionaryEntry(word, sb.toString());
	}
	
	private String loadHTMLFileContents(InputStream htmlStream) {
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
		
		return new String(buffer);
	}
}
