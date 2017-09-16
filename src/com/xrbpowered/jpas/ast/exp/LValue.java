package com.xrbpowered.jpas.ast.exp;

public abstract class LValue extends Expression {
	
	public abstract void assign(Object val);
	
}
