package com.xrbpowered.jpas.mem;

import com.xrbpowered.jpas.ast.data.Type;

public class StackFramePointer implements Pointer {

	public final Type type;
	public final StackFrame frame;
	public final int index;
	
	public StackFramePointer(Type type, StackFrame frame, int index) {
		this.type = type;
		this.frame = frame;
		this.index = index;
	}
	
	public Object read() {
		return frame.read(index);
	}
	
	public void write(Object value) {
		frame.write(index, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StackFramePointer) {
			StackFramePointer p = (StackFramePointer) obj;
			return frame==p.frame && index==p.index;
		}
		return false;
	}

}
