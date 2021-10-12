package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public abstract class IntMath extends Function {

	private final Type argType;
	
	public IntMath(Type argType) {
		this.argType = argType;
	}
	
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
		return argType;
	}

	public static void register(Scope global) {
		
		global.add("int", new IntMath(Type.real) {
			@Override
			public Object call(Object[] args) {
				return new Integer((int) ((double) args[0]));
			}
		});

		global.add("round", new IntMath(Type.real) {
			@Override
			public Object call(Object[] args) {
				return new Integer((int) Math.round((Double) args[0]));
			}
		});

	}
}
