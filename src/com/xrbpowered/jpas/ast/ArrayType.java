package com.xrbpowered.jpas.ast;

import java.util.List;

public class ArrayType extends Type {

	public final List<Range> ranges;
	public final Type type;
	
	public ArrayType(List<Range> ranges, Type t) {
		super(false, "array", null);
		this.ranges = ranges;
		this.type = t;
	}
	
	@Override
	public Object getDefValue() {
		return null; // TODO create array
	}
	
}
