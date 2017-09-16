package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.Type;

public class Equals extends BinaryOp {
	
	private final boolean neg;
	
	public Equals(Expression x, Expression y, boolean neg) {
		super(x, y);
		this.neg = neg;
	}
	
	@Override
	public Type getType() {
		return Type.bool;
	}
	
	@Override
	public Object evaluate() {
		Object xv = x.evaluate();
		Object yv = y.evaluate();
		if(xv==null || yv==null)
			return neg ^ new Boolean(xv==null && yv==null);
		else
			return neg ^ new Boolean(xv.equals(yv));
	}

}
