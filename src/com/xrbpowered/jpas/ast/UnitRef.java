package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;

public class UnitRef implements ScopeEntry {

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.unit;
	}

	@Override
	public boolean checkImpl() {
		return true;
	}

}
