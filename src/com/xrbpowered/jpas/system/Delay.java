package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.Scope.EntryType;
import com.xrbpowered.jpas.ast.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Delay extends Function {

	private static final Type[] argTypes = {Type.integer};
	
	@Override
	public EntryType getScopeEntryType() {
		return EntryType.procedure;
	}
	
	@Override
	public Type getType() {
		return null;
	}

	@Override
	public Type[] getArgTypes() {
		return argTypes;
	}

	@Override
	public Object call(Object[] args) {
		try {
			Thread.sleep((Integer) args[0]);
		}
		catch(InterruptedException e) {
		}
		return null;
	}

}
