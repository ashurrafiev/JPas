package com.xrbpowered.jpas.system.str;

import java.util.IllegalFormatException;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Format extends Function {

	@Override
	public Type getType() {
		return Type.string;
	}
	
	@Override
	public boolean isVarArgs() {
		return true;
	}
	
	@Override
	public int getArgNum() {
		return 2;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return Type.string;
	}

	@Override
	public Object call(Object[] args) {
		Object[] vals = new Object[args.length-1];
		for(int i=1; i<args.length; i++)
			vals[i-1] = args[i];
		try {
			return String.format((String) args[0], vals);
		}
		catch(IllegalFormatException e) {
			throw new JPasError("Format error.");
		}
	}
	
	public Function.Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		if(args!=null) {
			args[0] = checkTypeCast(scope, getArgType(0, args), args[0]);
			for(int i=1; i<args.length; i++) {
				args[i] = FunctionType.dereference(scope, args[i]);
				if(!args[i].getType().builtIn)
					throw JPasError.argumentTypeError();
			}
		}
		return new Function.Call(this, args);
	}

}
