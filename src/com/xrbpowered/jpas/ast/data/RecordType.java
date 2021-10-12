package com.xrbpowered.jpas.ast.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.xrbpowered.jpas.ast.IdMap;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.ast.exp.LValueRecordItem;
import com.xrbpowered.jpas.ast.exp.RecordItem;
import com.xrbpowered.jpas.mem.Pointer;

public class RecordType extends Type {

	private final boolean fluid;
	private IdMap<Integer> idMap = new IdMap<>();
	private List<Type> members = new ArrayList<>();
	
	public RecordType(boolean fluid) {
		super(false, null);
		this.fluid = fluid;
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
	
	public List<Type> memberTypes() {
		return members;
	}
	
	public Collection<String> fieldNames() {
		return idMap.map.keySet();
	}
	
	@Override
	public boolean isFluid() {
		return fluid;
	}

	@Override
	public boolean isSerialisable() {
		for(Type t : members)
			if(!t.isSerialisable())
				return false;
		return true;
	}
	
	@Override
	public boolean isInitialisable() {
		for(Type t : members)
			if(!t.isInitialisable())
				return false;
		return true;
	}
	
	@Override
	public Object init(Object v) {
		if(v==null)
			return new RecordObject(members);
		else
			return new RecordObject(members, (RecordObject) v);
	}
	
	@Override
	public void free(Pointer ptr) {
		((RecordObject) ptr.read()).free();
	}
	
	@Override
	public void assign(Pointer ptr, Object v) {
		RecordObject.copy(this, (RecordObject) ptr.read(), (RecordObject) v);
	}
	
	public void addLValueTo(LValue rec, Scope scope) {
		for(IdMap<Integer>.IdEntry e : idMap.map.values()) {
			scope.add(e.trueName, new LValueRecordItem(rec, e.e));
		}
	}
	
	public void addTo(Expression rec, Scope scope) {
		for(IdMap<Integer>.IdEntry e : idMap.map.values()) {
			scope.add(e.trueName, new RecordItem(rec, e.e));
		}
	}
	
}
