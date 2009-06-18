package com.amplio.rdict.tests;

import java.io.IOException;

import junit.framework.TestCase;
import com.strangegizmo.cdb.Cdb;

public class CDBTest extends TestCase {

	public void testNormalCdb(){
		
		Cdb db = null;
		try {
			db = new Cdb("test.cdb");
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
	
} 