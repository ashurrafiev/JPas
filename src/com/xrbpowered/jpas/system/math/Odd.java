package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Odd extends Function {

	private static final Type[] argTypes = {Type.integer};
	
	@Override
	public Type getType() {
		return Type.bool;
	}
	
	@Override
	public Type[] getArgTypes() {
		return argTypes;
	}
	@Override
	public Object call(Object[] args) {
		return new Boolean(((Integer) args[0] % 2) != 0);
	}

}
