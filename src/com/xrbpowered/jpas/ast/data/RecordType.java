package com.xrbpowered.jpas.ast.data;

import java.util.ArrayList;
import java.util.List;

import com.xrbpowered.jpas.ast.IdMap;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.ast.exp.LValueRecordItem;
import com.xrbpowered.jpas.mem.Pointer;

public class RecordType extends Type {

	private IdMap<Integer> idMap = new IdMap<>();
	private List<Type> members = new ArrayList<>();
	
	public RecordType() {
		super(false, null);
	}
	
	public void add(String name, Type type) {
		idMap.put(name, members.size());
		members.add(type);
	}
	
	public int find(String name) {
		IdMap<Integer>.IdEntry e = idMap.get(name.toLowerCase());
		return e==null ? -1 : e.e;
	}
	
	public Type getType(int index) {
		return members.get(index);
	}
	
	@Override
	public Object init(Object v) {
		RecordObject rec = new RecordObject(members);
		if(v!=null)
			RecordObject.copy(this, rec, (RecordObject) v);
		return rec;
	}
	
	@Override
	public void free(Pointer ptr) {
		((RecordObject) ptr.read()).free();
	}
	
	@Override
	public void assign(Pointer ptr, Object v) {
		RecordObject.copy(this, (RecordObject) ptr.read(), (RecordObject) v);
	}
	
	public void addTo(LValue rec, Scope scope) {
		for(IdMap<Integer>.IdEntry e : idMap.map.values()) {
			scope.add(e.trueName, new LValueRecordItem(rec, e.e));
		}
	}
	
}
