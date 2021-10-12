package com.xrbpowered.jpas.system.ord;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Ord extends Function {

	public static class Call extends Function.Call {

		public Call(Function f, Expression[] args) {
			super(f, args);
		}
		
		@Override
		public Object evaluate() {
			return ((Ord) f).call(args[0].getType(), args[0].evaluate());
		}
	}
	
	@Override
	public Type getType() {
		return Type.integer;
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
		return type.getOrdinator().ord(v);
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
			return new Ord.Call(this, args);
		else
			throw JPasError.argumentTypeError();
	}
}
