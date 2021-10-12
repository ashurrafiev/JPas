package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.ArrayItemPointer;
import com.xrbpowered.jpas.mem.Pointer;

public class LValueArrayItem extends LValue {
	
	private final LValue ar;
	private final Expression index;
	
	public LValueArrayItem(LValue ar, Expression index) {
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
		return false;
	}

	@Override
	public Pointer getPointer() {
		ArrayObject arv = (ArrayObject) ar.evaluate();
		Object iv = index.evaluate();
		return new ArrayItemPointer(arv, iv);
	}

}
