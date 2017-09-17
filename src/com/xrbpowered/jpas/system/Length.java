package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Length extends Function {

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
		return Type.string;
	}
	@Override
	public Object call(Object[] args) {
		return new Integer(((String) args[0]).length());
	}
}
