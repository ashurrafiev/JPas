package com.xrbpowered.jpas.ast.data;

public abstract class IndexableType extends Type {

	protected IndexableType(boolean builtIn, Object defValue) {
		super(builtIn, defValue);
	}
	
	public abstract Type indexType();
}
