package com.amplio.rdict.tests;

import com.amplio.rdict.Headword;

import junit.framework.TestCase;

public class HeadwordTest extends TestCase {
	
	public void testConstructor() {
		long id = 1;
		String headword = "fish";
		String contents = "this is the contents of the entry for the headword fish";
		
		Headword hw = new Headword(id, headword, contents);
		
		assertEquals(id, hw.id);
		assertEquals(headword, hw.headword);
		assertEquals(contents, hw.contents);
	}

}
