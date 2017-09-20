package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.Type;

public class Equals extends BinaryOp {
	
	private final boolean neg;
	
	public Equals(Type type, Expression x, Expression y, boolean neg) {
		super(type, x, y);
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
		return neg ^ new Boolean(test(xv, yv));
	}
	
	public static boolean test(Object xv, Object yv) {
		if(xv==null || yv==null)
			return xv==null && yv==null;
		else
			return xv.equals(yv);
	}

}
