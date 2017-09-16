package com.xrbpowered.jpas.mem;

import java.util.ArrayList;
import java.util.LinkedList;

public class StackFrameDesc {

	private ArrayList<StackFrameObject> objects = new ArrayList<>();
	private LinkedList<StackFrame> frames = new LinkedList<>();
	
	public int register(StackFrameObject sfo) {
		objects.add(sfo);
		return size()-1;
	}
	
	public int size() {
		return objects.size();
	}
	
	public void alloc() {
		frames.add(new StackFrame(objects.size()));
	}
	
	public void release() {
		frames.removeLast();
	}
	
	public Object read(int index) {
		return frames.getLast().values[index];
	}
	
	public void write(int index, Object value) {
		frames.getLast().values[index] = value;
	}
	
}
