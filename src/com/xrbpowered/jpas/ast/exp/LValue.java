package com.xrbpowered.jpas.ast.exp;

public abstract class LValue extends Expression {
	
	public abstract void init(Object val); // TODO not in lvalue? stackframeobject?
	public abstract void assign(Object val);
	
}
