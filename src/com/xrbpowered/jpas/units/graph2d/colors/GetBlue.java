package com.xrbpowered.jpas.units.graph2d.colors;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;

public class GetBlue extends StdFunction {

	public GetBlue() {
		super(Type.real, new Type[] {Type.integer});
	}
	
	@Override
	public boolean hasSideEffects() {
		return false;
	}
	
	@Override
	public Object call(Object[] args) {
		int color = (Integer) args[0];
		return new Double((color & 0xff) / 255.0);
	}

}
