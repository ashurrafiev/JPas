package com.xrbpowered.jpas.mem;

import com.xrbpowered.jpas.ast.data.Type;

public class FreePointer implements Pointer {

	private Object value;
	
	public FreePointer(Type type) {
		value = type.init(null);
	}
	
	public FreePointer(Type type, Object v) {
		value = type.init(v);
	}
	
	@Override
	public Object read() {
		return value;
	}

	@Override
	public void write(Object value) {
		this.value = value;
	}

}
