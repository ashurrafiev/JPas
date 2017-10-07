package com.xrbpowered.jpas.mem;

import java.util.ArrayList;
import java.util.LinkedList;

import com.xrbpowered.jpas.ast.data.Type;

public class StackFrameDesc {

	private ArrayList<StackFrameObject> objects = new ArrayList<>();
	private LinkedList<StackFrame> frames = new LinkedList<>();
	
	private final StackFrameDesc parent;
	
	public StackFrameDesc(StackFrameDesc parent) {
		this.parent = parent;
	}
	
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
		frames.removeLast().free();
	}
	
	public Pointer getPointer(Type type, int index) {
		return new StackFramePointer(type, frames.getLast(), index);
	}
	
	public Object read(int index) {
		return frames.getLast().read(index);
	}
	
	public void write(int index, Object value) {
		frames.getLast().write(index, value);
	}
	
	public StackFrameDesc getParent() {
		return parent;
	}
	
	public boolean isVisible(StackFrameDesc sf) {
		if(sf==this)
			return true;
		else if(parent!=null)
			return parent.isVisible(sf);
		else
			return false;
	}
	
}
