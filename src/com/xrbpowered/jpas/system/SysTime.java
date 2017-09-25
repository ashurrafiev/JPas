package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class SysTime extends Function {

	@Override
	public Type getType() {
		return Type.integer;
	}

	@Override
	public int getArgNum() {
		return 0;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return null;
	}

	@Override
	public boolean hasSideEffects() {
		return true;
	}

	@Override
	public Object call(Object[] args) {
		return new Integer((int)(System.currentTimeMillis() / 1000L));
	}

}
