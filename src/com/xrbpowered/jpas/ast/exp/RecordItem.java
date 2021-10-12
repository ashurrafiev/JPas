package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.data.RecordObject;
import com.xrbpowered.jpas.ast.data.RecordType;
import com.xrbpowered.jpas.ast.data.Type;

public class RecordItem extends Expression implements ScopeEntry {

	private final Expression rec;
	private final int index;
	
	public RecordItem(Expression rec, int index) {
		this.rec = rec;
		this.index = index;
	}
	
	@Override
	public EntryType getScopeEntryType() {
		return EntryType.variable;
	}

	@Override
	public boolean checkImpl() {
		return true;
	}
	
	@Override
	public Type getType() {
		return ((RecordType) rec.getType()).getType(index);
	}

	@Override
	public Object evaluate() {
		RecordObject recv = (RecordObject) rec.evaluate();
		return recv.read(index);
	}

	@Override
	public boolean isConst() {
		return rec.isConst();
	}

}
