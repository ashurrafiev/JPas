package com.xrbpowered.jpas.mem;

public interface StackFrameObject {
	
	public int register(StackFrameDesc sf);
	public void init(Object val);
	public void free();

}
