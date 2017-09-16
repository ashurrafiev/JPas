package com.xrbpowered.jpas.ast.data;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Range;

public class ArrayObject {

	public final Range.Fixed range;
	public final Object[] values;
	
	public ArrayObject(Range.Fixed range) {
		this.range = range;
		this.values = new Object[range.length()];
	}
	
	private int calcIndex(Object index) {
		int i = range.type.getOrdinator().ord(index) - range.min;
		if(i<0 || i>=values.length)
			throw new JPasError("Range check error.");
		return i;
	}
	
	public Object read(Object index) {
		return values[calcIndex(index)];
	}

	public void write(Object index, Object val) {
		values[calcIndex(index)] = val;
	}

}
