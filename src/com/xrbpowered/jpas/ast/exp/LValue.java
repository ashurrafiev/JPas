package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.mem.Pointer;

public abstract class LValue extends Expression {
	
	public abstract Pointer getPointer();
	
	public void assign(Object value) {
		getType().assign(getPointer(), value);
	}
	
}
