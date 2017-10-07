package com.xrbpowered.jpas.system.ord;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.data.Type.Ordinator;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class SuccPred extends Function {

	public static class Call extends Function.Call {

		public Call(Function f, Expression[] args) {
			super(f, args);
		}
		
		@Override
		public Type getType() {
			return args[0].getType();
		}
		
		@Override
		public Object evaluate() {
			return ((SuccPred) f).call(args[0].getType(), args[0].evaluate());
		}
	}
	
	protected final boolean inc;
	
	public SuccPred(boolean inc) {
		this.inc = inc;
	}

	@Override
	public Type getType() {
		return null;
	}

	@Override
	public int getArgNum() {
		return 1;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return null;
	}
	
	public Object call(Type type, Object v) {
		Ordinator ord = type.getOrdinator();
		return inc ? ord.succ(v) : ord.pred(v);
	}
	
	@Override
	public Object call(Object[] args) {
		return null;
	}

	public Function.Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		args[0] = FunctionType.dereference(scope, args[0]);
		Type type = args[0].getType();
		if(type.getOrdinator()!=null)
			return new SuccPred.Call(this, args);
		else
			throw JPasError.argumentTypeError();
	}
}
