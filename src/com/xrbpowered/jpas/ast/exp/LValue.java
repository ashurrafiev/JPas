package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.mem.Pointer;

public abstract class LValue extends Expression {
	
	public abstract void init(Object val); // TODO not in lvalue? stackframeobject?
	public abstract Pointer getPointer();
	
	public void assign(Object value) {
		getType().assign(getPointer(), value);
	}
	
}
