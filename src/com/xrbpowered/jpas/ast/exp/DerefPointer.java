package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;

public class DerefPointer extends LValue {

	private final Type type;
	private final Expression ex;
	
	public DerefPointer(Type type, Expression ex) {
		this.type = type;
		this.ex = ex;
	}
	
	@Override
	public Pointer getPointer() {
		Pointer p = (Pointer) ex.evaluate();
		if(p==null)
			throw new JPasError("Pointer is nil.");
		return p;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public Object evaluate() {
		return getPointer().read();
	}

	@Override
	public boolean isConst() {
		return false;
	}

}
