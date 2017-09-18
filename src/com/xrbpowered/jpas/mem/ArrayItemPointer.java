package com.xrbpowered.jpas.mem;

import com.xrbpowered.jpas.ast.data.ArrayObject;

public class ArrayItemPointer implements Pointer {

	private final ArrayObject ar;
	private final Object index;
	
	public ArrayItemPointer(ArrayObject ar, Object index) {
		this.ar = ar;
		this.index = index;
	}
	
	@Override
	public Object read() {
		return ar.read(index);
	}
	
	@Override
	public void write(Object value) {
		ar.write(index, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ArrayItemPointer) {
			ArrayItemPointer p = (ArrayItemPointer) obj;
			return ar==p.ar && index==p.index;
		}
		return false;
	}
	
}
