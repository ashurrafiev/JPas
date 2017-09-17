package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Delay extends Function {

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.procedure;
	}
	
	@Override
	public Type getType() {
		return null;
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
		try {
			Thread.sleep((Integer) args[0]);
		}
		catch(InterruptedException e) {
		}
		return null;
	}

}
