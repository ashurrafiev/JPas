package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Type;

public class CharOfString extends Expression {

	private final Expression s;
	private final Expression index;
	
	public CharOfString(Expression s, Expression index) {
		this.s = s;
		this.index = index;
	}
	
	@Override
	public Type getType() {
		return Type.string;
	}

	@Override
	public boolean isConst() {
		return s.isConst() && index.isConst();
	}
	
	@Override
	public Object evaluate() {
		String str = (String) s.evaluate();
		int i = (Integer) index.evaluate();
		return str.substring(i-1, i);
	}

}
