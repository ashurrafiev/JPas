package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.Type;

public abstract class CompareOp extends BinaryOp {

	public CompareOp(Expression x, Expression y) {
		super(x, y);
	}
	
	@Override
	public Type getType() {
		return Type.bool;
	}

	protected abstract boolean checkRes(int res);
	
	@Override
	public Object evaluate() {
		return new Boolean(checkRes(x.getType().getComparator().compare(x.evaluate(), y.evaluate())));
	}
	
	public static Expression make(Operation op, Expression x, Expression y) {
		if(x.getType().getComparator()==null)
			return null;
		switch(op) {
			case gt:
				return new CompareOp(x, y) {
					@Override
					protected boolean checkRes(int res) {
						return res>0;
					}
				};
			case lt:
				return new CompareOp(x, y) {
					@Override
					protected boolean checkRes(int res) {
						return res<0;
					}
				};
			case ge:
				return new CompareOp(x, y) {
					@Override
					protected boolean checkRes(int res) {
						return res>=0;
					}
				};
			case le:
				return new CompareOp(x, y) {
					@Override
					protected boolean checkRes(int res) {
						return res<=0;
					}
				};
			default:
				return null;
		}
	}
}
