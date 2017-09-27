package com.xrbpowered.jpas.ast.data;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;

public class ForwardPointerType extends PointerType {

	private final Scope scope;
	private final String name;
	
	public ForwardPointerType(Scope scope, String name) {
		super(null);
		this.scope = scope;
		this.name = name;
	}
	
	@Override
	public boolean checkImpl() {
		if(type==null) {
			ScopeEntry e = scope.find(name);
			if(e==null || e.getScopeEntryType()!=EntryType.type)
				throw new JPasError("Type "+name+" undefined");
			else
				type = (Type) e;
		}
		return true;
	}
	
	public Type getType() {
		checkImpl();
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		checkImpl();
		return super.equals(obj);
	}
}
