package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.ast.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Length extends Function {

	private static final Type[] argTypes = {Type.string};
	
	@Override
	public Type getType() {
		return Type.integer;
	}

	@Override
	public Type[] getArgTypes() {
		return argTypes;
	}

	@Override
	public Object call(Object[] args) {
		return new Integer(((String) args[0]).length());
	}
}
