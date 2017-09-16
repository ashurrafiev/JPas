package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public abstract class IntMath extends Function {

	private final Type[] argTypes;
	
	public IntMath(Type argType) {
		this.argTypes = new Type[] {argType};
	}
	
	@Override
	public Type getType() {
		return Type.integer;
	}
	
	@Override
	public Type[] getArgTypes() {
		return argTypes;
	}
	
	public static void register(Scope global) {
		
		global.addFunction("int", new IntMath(Type.real) {
			@Override
			public Object call(Object[] args) {
				return new Integer((int) ((double) args[0]));
			}
		});

		global.addFunction("round", new IntMath(Type.real) {
			@Override
			public Object call(Object[] args) {
				return new Integer((int) Math.round((Double) args[0]));
			}
		});

	}
}
