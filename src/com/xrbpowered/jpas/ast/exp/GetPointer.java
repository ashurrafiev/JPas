package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.PointerType;
import com.xrbpowered.jpas.ast.data.Type;

public class GetPointer extends Expression {

	private final LValue lvalue;
	
	public GetPointer(LValue lvalue) {
		this.lvalue = lvalue;
	}
	
	@Override
	public Type getType() {
		return new PointerType(lvalue.getType());
	}

	@Override
	public Object evaluate() {
		return lvalue.getPointer();
	}

	@Override
	public boolean isConst() {
		return false;
	}

}
