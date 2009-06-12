package com.amplio.rdict.tests;

import java.io.FileInputStream;
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
		
		db.close();
		
		assertEquals("flam", value);
		
	}
	
} 