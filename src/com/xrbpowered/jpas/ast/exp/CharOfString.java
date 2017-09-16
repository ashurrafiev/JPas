package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type;

public class CharOfString extends Expression {

	private final Expression s;
	private final Expression index;
	
	public CharOfString(Expression s, Expression index) {
		this.s = s;
		this.index = index;
	}
	
	@Override
	public Type getType() {
		return Type.character;
	}

	@Override
	public boolean isConst() {
		return s.isConst() && index.isConst();
	}
	
	@Override
	public Object evaluate() {
		String str = (String) s.evaluate();
		int i = (Integer) index.evaluate();
		if(i<1 || i>str.length())
			throw new JPasError("Range check error");
		return new Character(str.charAt(i-1));
	}

}
