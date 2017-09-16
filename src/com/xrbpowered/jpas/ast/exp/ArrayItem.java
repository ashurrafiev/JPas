package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.Type;

public class ArrayItem extends LValue {

	private final LValue ar;
	private final Expression index;
	
	public ArrayItem(LValue ar, Expression index) {
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(Object val) {
	}

	@Override
	public void assign(Object val) {
		ArrayObject arv = (ArrayObject) ar.evaluate();
		Object iv = index.evaluate();
		arv.write(iv, ((ArrayType) ar.getType()).type.assign(arv.read(iv), val));
	}

}
