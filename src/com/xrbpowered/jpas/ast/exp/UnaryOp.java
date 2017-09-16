package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.Type;

public abstract class UnaryOp extends Expression {

	public static enum Operation {
		pos, neg, not
	}

	protected Expression x;
	
	public UnaryOp(Expression x) {
		this.x = x;
	}
	
	@Override
	public Type getType() {
		return x.getType();
	}
	
	@Override
	public boolean isConst() {
		return x.isConst();
	}
	
	public static Expression make(Operation op, Expression x) {
		Type xt = x.getType();
		switch(op) {
			case pos:
				if(xt==Type.integer || xt==Type.real)
					return x;
				else
					return null;
			
			case neg:
				if(xt==Type.integer)
					return new UnaryOp(x) { // -int
						@Override
						public Object evaluate() {
							return -((Integer) x.evaluate());
						}
					};
				else if(xt==Type.real)
					return new UnaryOp(x) { // -real
						@Override
						public Object evaluate() {
							return -((Double) x.evaluate());
						}
					};
				else
					return null;

			case not:
				if(xt==Type.integer)
					return new UnaryOp(x) { // not int
						@Override
						public Object evaluate() {
							return ~((Integer) x.evaluate());
						}
					};
				else if(xt==Type.bool)
					return new UnaryOp(x) { // not bool
						@Override
						public Object evaluate() {
							return !((Boolean) x.evaluate());
						}
					};
				else
					return null;

			default:
				return null;
		}
	}
}
