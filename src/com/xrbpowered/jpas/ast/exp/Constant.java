package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.data.PointerType;
import com.xrbpowered.jpas.ast.data.Type;

public class Constant extends Expression implements ScopeEntry {

	private final Object val;
	private final Type type;
	
	public Constant(Type type, Object val) {
		this.type = type;
		this.val = val;
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
			int v;
			boolean ch = false;
			if(s.charAt(0)=='#') {
				ch = true;
				s = s.substring(1);
			}
			if(s.charAt(0)=='$') {
				v = Integer.parseInt(s.substring(1), 16);
			}
			else
				v = Integer.parseInt(s);
			if(ch)
				return new Constant(Type.real, new Character((char) v));
			else
				return new Constant(Type.integer, v);
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
	
	public static final Constant constTrue = new Constant(Type.bool, true);
	public static final Constant constFalse = new Constant(Type.bool, false);
	public static final Constant constNil = new Constant(new PointerType(null), null);
	public static final Constant constPi = new Constant(Type.real, Math.PI);
}
