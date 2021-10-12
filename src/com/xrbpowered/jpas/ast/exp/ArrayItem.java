package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.Type;

public class ArrayItem extends Expression {

	private final Expression ar;
	private final Expression index;
	
	public ArrayItem(Expression ar, Expression index) {
		this.ar = ar;
		this.index = index;
	}
	
	@Override
	public Type getType() {
		return ((ArrayType) ar.getType()).type;
	}

	@Override
	public Object evaluate() {
		ArrayObject arv = (ArrayObject) ar.evaluate();
		Object iv = index.evaluate();
		return arv.read(iv);
	}

	@Override
	public boolean isConst() {
		return ar.isConst() && index.isConst();
	}

}
