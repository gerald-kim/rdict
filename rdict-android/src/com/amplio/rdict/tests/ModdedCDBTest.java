package com.amplio.rdict.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import junit.framework.TestCase;

import com.strangegizmo.cdb.ModdedCdb;

public class ModdedCDBTest extends TestCase {

	public void testNormalCdb(){
		
		ModdedCdb db = null;
		try {
			db = new ModdedCdb(new FileInputStream("test.cdb"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String key = "flim";
		
		String value = new String(db.find(key.getBytes()));
		
		assertEquals("flam", value);
		
		value = new String(db.find(key.getBytes()));
		
		db.close();
		
		assertEquals("flam", value);
		
	}
	
	public void testNotResettingInputStreamMakesSearchFail() {
		
		FileInputStream fis = null;
		
		ModdedCdb db = null;
		try {
			fis = new FileInputStream("test.cdb");
			db = new ModdedCdb(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String key = "flim";
		
		String value = new String(db.find(key.getBytes()));
		
		assertEquals("flam", value);
		
		try {
			db = new ModdedCdb(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		value = new String(db.find(key.getBytes()));
		
		assertEquals("flam", value);
	}
	
} 