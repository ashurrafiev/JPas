package com.xrbpowered.jpas.ast.data;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Range;
import com.xrbpowered.jpas.ast.exp.Equals;
import com.xrbpowered.jpas.mem.ArrayItemPointer;

public class ArrayObject {

	public final Range.Fixed range;
	private Object[] values;
	
	public ArrayObject(Range.Fixed range, Type type) {
		this.range = range;
		this.values = new Object[range.length()];
		for(int i=0; i<values.length; i++)
			values[i] = type.init(null);
	}

	public void free() {
		this.values = null;
	}
	
	private void check() {
		if(values==null)
			throw new JPasError("Access violation.");
	}
	
	private int calcIndex(Object index) {
		int i = range.type.getOrdinator().ord(index) - range.min;
		if(i<0 || i>=values.length)
			throw new JPasError("Range check error.");
		return i;
	}
	
	public Object read(Object index) {
		check();
		return values[calcIndex(index)];
	}

	public void write(Object index, Object val) {
		check();
		values[calcIndex(index)] = val;
	}

	@Override
	public boolean equals(Object obj) {
		if(this==obj)
			return true;
		if(obj instanceof ArrayObject) {
			ArrayObject ar = (ArrayObject) obj;
			check();
			ar.check();
			for(int i=0; i<values.length; i++)
				if(!Equals.test(values[i], ar.values[i]))
					return false;
			return true;
		}
		return false;
	}

	public static void copy(ArrayType type, ArrayObject dst, ArrayObject src) {
		if(type.type instanceof ArrayType) {
			for(int i=0; i<dst.values.length; i++)
				copy((ArrayType) type.type, (ArrayObject) dst.values[i], (ArrayObject) src.values[i]);
		}
		else {
			for(int i=0; i<dst.values.length; i++)
				type.type.assign(new ArrayItemPointer(dst, type.range.indexFor(i)), src.values[i]);
		}
	}
	
}
