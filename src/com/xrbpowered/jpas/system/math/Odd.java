package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Odd extends Function {

	@Override
	public Type getType() {
		return Type.bool;
	}
	
	@Override
	public int getArgNum() {
		return 1;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return Type.integer;
	}

	@Override
	public Object call(Object[] args) {
		return new Boolean(((Integer) args[0] % 2) != 0);
	}

}
