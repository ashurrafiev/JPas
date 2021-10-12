package com.xrbpowered.jpas.units.graph2d.input;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class KeyDown extends StdFunction {

	public KeyDown() {
		super(Type.bool, new Type[] {Type.integer});
	}
	
	@Override
	public Object call(Object[] args) {
		return Graph2D.unit.input.isKeyDown((Integer) args[0]);
	}

}
