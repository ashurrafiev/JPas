package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;

public class Length extends Function {

	public static class LengthArray extends Length {
		@Override
		public Object call(Object[] args) {
			ArrayObject ar = (ArrayObject) args[0];
			return new Integer(ar.range.length());
		}
	}
	
	private static final LengthArray lengthArray = new LengthArray();
	
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
	
	@Override
	public Object call(Object[] args) {
		return new Integer(((String) args[0]).length());
	}
	
	@Override
	public Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		args[0] = FunctionType.dereference(scope, args[0]);
		if(args[0].getType() instanceof ArrayType) {
			if(!(args[0] instanceof LValue))
				throw JPasError.lvalueError();
			return new Function.Call(lengthArray, args); 
		}
		args[0] = checkTypeCast(scope, Type.string, args[0]);
		return new Function.Call(this, args);
	}
}
