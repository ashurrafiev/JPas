package com.xrbpowered.jpas.ast;

import java.util.HashMap;

import com.xrbpowered.jpas.JPasError;

public class IdMap<T> {

	public class IdEntry {
		public final String trueName;
		public final T e;
		
		public IdEntry(String name, T e) {
			this.trueName = name;
			this.e = e;
		}
	}
	
	public HashMap<String, IdEntry> map = new HashMap<>(); 
	
	public IdEntry get(String name) {
		return map.get(name);
	}

	public void put(String name, T e) {
		String n = name.toLowerCase();
		if(map.containsKey(n))
			throw new JPasError("Duplicate identifier: "+name);
		else
			map.put(n, new IdEntry(name, e));
	}
	
	public int size() {
		return map.size();
	}
}
