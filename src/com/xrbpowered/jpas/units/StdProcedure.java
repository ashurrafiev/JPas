package com.xrbpowered.jpas.units;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;

public abstract class StdProcedure extends StdFunction {

	public StdProcedure(Type[] argTypes) {
		super(null, argTypes);
	}

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.procedure;
	}
	
}
