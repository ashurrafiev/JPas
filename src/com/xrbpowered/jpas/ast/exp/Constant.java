package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Type;

public class Constant extends Expression {

	private final Object val;
	private final Type type;
	
	public Constant(Type type, Object val) {
		this.type = type;
		this.val = val;
	}
	
	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	public Object evaluate() {
		return val;
	}
	
	@Override
	public boolean isConst() {
		return true;
	}
	
	public static Constant parseNumber(String s) {
		try {
			return new Constant(Type.integer, Integer.parseInt(s));
		}
		catch(NumberFormatException ei) {
			try {
				return new Constant(Type.real, Double.parseDouble(s));
			}
			catch(NumberFormatException ed) {
				return null;
			}
		}
	}
}
