package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Halt extends Function {

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
		return 0;
	}

	@Override
	public Type getArgType(int argIndex) {
		return null;
	}

	@Override
	public Object call(Object[] args) {
		System.exit(0);
		return null;
	}

}
