package com.xrbpowered.jpas.ast.data;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.exp.Equals;
import com.xrbpowered.jpas.mem.ArrayItemPointer;

public class ArrayObject {

	public final Range range;
	private Object[] values;

	public ArrayObject(Range range, Type type) {
		if(range==null)
			throw new JPasError("Unknown range, cannot create array.");
		this.range = range;
		this.values = new Object[range.length()];
		for(int i=0; i<values.length; i++)
			values[i] = type.init(null);
	}

	public ArrayObject(Range range, Type type, ArrayObject v) {
		if(range==null)
			throw new JPasError("Unknown range, cannot create array.");
		this.range = range;
		this.values = new Object[range.length()];
		if(values.length<v.values.length)
			JPasError.rangeCheckError();
		for(int i=0; i<values.length; i++)
			values[i] = i>=v.values.length ? type.init(null) : type.init(v.values[i]);
	}

	public ArrayObject(Range range, Object[] v) {
		this.range = range;
		this.values = v;
	}
	
	public Object[] getValues() {
		check();
		return values;
	}
	
	public void free() {
		this.values = null;
	}
	
	public void check() {
		if(values==null)
			throw new JPasError("Access violation.");
	}
	
	private int calcIndex(Object index) {
		int i = range==null ? (Integer) index : (range.type.getOrdinator().ord(index) - range.min);
		if(i<0 || i>=values.length)
			JPasError.rangeCheckError();
		return i;
	}
	
	public Object get(int index) {
		return values[index];
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
		dst.check();
		src.check();
		if(dst.values.length<src.values.length)
			JPasError.rangeCheckError();
		if(type.type instanceof ArrayType) {
			for(int i=0; i<dst.values.length; i++)
				copy((ArrayType) type.type, (ArrayObject) dst.values[i], (ArrayObject) src.values[i]);
		}
		else {
			for(int i=0; i<src.values.length; i++)
				type.type.assign(new ArrayItemPointer(dst, type.range==null ? i : type.range.indexFor(i)), src.values[i]);
		}
	}
	
	public static void fill(ArrayType type, ArrayObject dst, Object v, int depth) {
		dst.check();
		if(depth>0) {
			for(int i=0; i<dst.values.length; i++)
				fill((ArrayType) type.type, (ArrayObject) dst.values[i], v, depth-1);
		}
		else {
			for(int i=0; i<dst.values.length; i++)
				type.type.assign(new ArrayItemPointer(dst, type.range==null ? i : type.range.indexFor(i)), v);
		}
	}
	
}
