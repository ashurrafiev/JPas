package com.xrbpowered.jpas.mem;

import com.xrbpowered.jpas.ast.data.Type;

public class Pointer {

	public final Type type;
	public final StackFrame frame;
	public final int index;
	
	public Pointer(Type type, StackFrame frame, int index) {
		this.type = type;
		this.frame = frame;
		this.index = index;
	}
	
	public Object read() {
		return frame.values[index];
	}
	
	public void write(Object value) {
		frame.values[index] = value;
	}

	
}
