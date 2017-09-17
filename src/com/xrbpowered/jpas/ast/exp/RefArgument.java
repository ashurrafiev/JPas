package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;
import com.xrbpowered.jpas.mem.StackFrameDesc;

public class RefArgument extends Variable {

	public RefArgument(Type type, StackFrameDesc sf) {
		super(type, sf);
	}
	
	public void setPointer(Pointer ptr) {
		sf.write(sfIndex, ptr);
	}
	
	@Override
	public void init(Object val) {
	}

	@Override
	public Pointer getPointer() {
		return (Pointer) sf.read(sfIndex);
	}
	
	@Override
	public Object evaluate() {
		return getPointer().read();
	}

}
