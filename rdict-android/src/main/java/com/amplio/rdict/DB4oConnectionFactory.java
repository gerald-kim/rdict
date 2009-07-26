package com.amplio.rdict;

import java.util.HashMap;
import java.util.Map;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

public class DB4oConnectionFactory {

	private static Map<String, ObjectContainer> cache = new HashMap<String, ObjectContainer>();

	public static ObjectContainer getObjectContainer(String db) {
		ObjectContainer container = cache.get(db);
		
		if(container != null) {
			return container;
		}
		else {
			container = Db4o.openFile(db);
			cache.put(db, container);
			
			return container;
		}
	}
}
