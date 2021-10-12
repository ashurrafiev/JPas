package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.RangeType;
import com.xrbpowered.jpas.ast.data.Type;

public abstract class Expression {

	private static class Call extends Statement {
		private final Expression ex;
		
		public Call(Expression ex) {
			this.ex = ex;
		}
		
		@Override
		public String execute() {
			ex.evaluate();
			return null;
		}
	}
	
	public abstract Type getType();
	public abstract Object evaluate();
	public abstract boolean isConst();
	
	public static Expression implicitCast(Scope scope, Type dt, Expression src) {
		Type st = src.getType();
		
		if(st instanceof FunctionType) {
			if(dt instanceof FunctionType) {
				if(dt.equals(st))
					return src;
				else
					return null;
			}
			else {
				src = ((FunctionType) st).makeCall(scope, src, null);
				st = src.getType();
			}
		}
		
		if(st.isFluid())
			return ((FluidTypeExpression) src).backPropagateType(scope, dt);
		else if(dt.equals(st))
			return src;
		else if(dt==Type.string)
			return ToString.make(src);
		else if(dt==Type.real && st==Type.integer)
			return IntAsReal.make(src);
		else if(dt instanceof RangeType) {
			RangeType rt = (RangeType) dt;
			if(rt.getBaseType().equals(st))
				return new RangeCheck(rt.range, src);
			else
				return null;
		}
		else if(st instanceof RangeType) {
			RangeType rt = (RangeType) st;
			if(dt.equals(rt.getBaseType()))
				return src;
			else
				return null;
		}
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
	
	public static Statement call(Expression ex) {
		if(ex.isConst())
			return Statement.nop;
		else
			return new Call(ex);
	}
	
}
