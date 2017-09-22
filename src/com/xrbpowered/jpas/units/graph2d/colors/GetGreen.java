package com.xrbpowered.jpas.units.graph2d.colors;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;

public class GetGreen extends StdFunction {

	public GetGreen() {
		super(Type.real, new Type[] {Type.integer});
	}
	
	@Override
	public boolean hasSideEffects() {
		return false;
	}
	
	@Override
	public Object call(Object[] args) {
		int color = (Integer) args[0];
		return new Double(((color >> 8) & 0xff) / 255.0);
	}

}
