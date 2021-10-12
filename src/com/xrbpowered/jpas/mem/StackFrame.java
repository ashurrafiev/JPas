package com.xrbpowered.jpas.mem;

import com.xrbpowered.jpas.JPasError;

public class StackFrame {

	private Object[] values;
	
	public StackFrame(int size) {
		this.values = new Object[size];
	}
	
	public void free() {
		values = null;
	}
	
	private void check() {
		if(values==null)
			throw new JPasError("Access violation.");
	}
	
	public Object read(int index) {
		check();
		return values[index];
	}

	public void write(int index, Object val) {
		check();
		values[index] = val;
	}
	
}
