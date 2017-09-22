package com.xrbpowered.jpas.units;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public abstract class StdFunction extends Function {

	protected final Type type;
	protected final Type[] argTypes;
	
	public StdFunction(Type type, Type[] argTypes) {
		this.type = type;
		this.argTypes = argTypes;
	}
	
	@Override
	public Type getType() {
		return type;
	}

	@Override
	public int getArgNum() {
		return argTypes.length;
	}

	@Override
	public Type getArgType(int argIndex) {
		return argTypes[argIndex];
	}
	
	@Override
	public boolean hasSideEffects() {
		return true;
	}

}
