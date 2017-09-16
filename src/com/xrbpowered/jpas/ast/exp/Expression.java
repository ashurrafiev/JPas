package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.ast.data.Type;

public abstract class Expression {

	public static class Call extends Statement {
		private final Expression ex;
		
		public Call(Expression ex) {
			this.ex = ex;
		}
		
		@Override
		public void execute() {
			ex.evaluate();
		}
	}
	
	public abstract Type getType();
	public abstract Object evaluate();
	public abstract boolean isConst();
	
	public static Expression implicitCast(Type dt, Expression src) {
		Type st = src.getType();
		if(dt.equals(st))
			return src;
		else if(dt==Type.string)
			return ToString.make(src); // TODO remove implicit cast to String
		else if(dt==Type.real && st==Type.integer)
			return IntAsReal.make(src);
		else
			return null;
	}
	
	public static Expression precalc(Expression ex) {
		if(ex==null)
			return null;
		if(ex.isConst() && !(ex instanceof Constant)) {
			return new Constant(ex.getType(), ex.evaluate());
		}
		else
			return ex;
	}
	
}
