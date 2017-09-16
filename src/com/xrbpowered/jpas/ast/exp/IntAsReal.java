package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.Type;

public class IntAsReal extends Expression {

	private Expression x;
	
	public IntAsReal(Expression x) {
		this.x = x;
	}
	
	@Override
	public Type getType() {
		return Type.real;
	}
	
	@Override
	public Object evaluate() {
		return new Double((Integer) x.evaluate());
	}
	
	@Override
	public boolean isConst() {
		return x.isConst();
	}

	public static Expression make(Expression x) {
		Type xt = x.getType();
		if(xt==Type.real)
			return x;
		else if(xt==Type.integer) {
			return new IntAsReal(x);
		}
		else
			return null;
	}
	
}
