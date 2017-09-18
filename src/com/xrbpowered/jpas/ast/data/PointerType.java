package com.xrbpowered.jpas.ast.data;

public class PointerType extends Type {

	public final Type type;
	
	public PointerType(Type t) {
		super(false, null);
		this.type = t;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			if(obj instanceof PointerType) {
				PointerType pt = (PointerType) obj;
				return type==null || pt.type==null || type.equals(pt.type);
			}
			return false;
		}
		else
			return true;
	}
	
}
