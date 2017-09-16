package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Type;

public class ToString extends Expression {

	private Expression x;
	
	public ToString(Expression x) {
		this.x = x;
	}
	
	@Override
	public Type getType() {
		return Type.string;
	}

	@Override
	public Object evaluate() {
		return x.evaluate().toString();
	}
	
	@Override
	public boolean isConst() {
		return x.isConst();
	}

	public static Expression make(Expression x) {
		Type xt = x.getType();
		if(xt==Type.string)
			return x;
		else if(xt.builtIn)
			return new ToString(x);
		else
			return null;
	}
}
