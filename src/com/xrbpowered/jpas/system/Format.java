package com.xrbpowered.jpas.system;

import java.util.IllegalFormatException;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Format extends Function {

	private static final Type[] argTypes = {Type.string, Type.string};
	
	@Override
	public Type getType() {
		return Type.string;
	}
	
	@Override
	public boolean isVarArgs() {
		return true;
	}
	
	@Override
	public Type[] getArgTypes() {
		return argTypes;
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
	
	public Function.Call makeCall(Expression[] args) {
		testArgNumber(getArgTypes().length, args);
		if(args!=null) {
			args[0] = checkTypeCast(getArgType(0, args), args[0]);
			for(int i=1; i<args.length; i++) {
				if(!args[i].getType().builtIn)
					throw new JPasError("Argument type mismatch");
			}
		}
		return new Function.Call(this, args);
	}

}
