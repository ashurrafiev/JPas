package com.xrbpowered.jpas.mem;

public class StackFrame {

	public final Object[] values;
	
	public StackFrame(int size) {
		this.values = new Object[size];
	}
	
}
