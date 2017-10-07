package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Str extends Function {

	@Override
	public Type getType() {
		return Type.string;
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
		return args[0].toString();
	}
	
	public Function.Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		args[0] = FunctionType.dereference(scope, args[0]);
		if(!args[0].getType().builtIn)
			throw JPasError.argumentTypeError();
		return new Function.Call(this, args);
	}

}
