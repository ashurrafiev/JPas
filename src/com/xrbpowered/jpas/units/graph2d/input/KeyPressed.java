package com.xrbpowered.jpas.units.graph2d.input;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class KeyPressed extends StdFunction {

	public KeyPressed() {
		super(Type.bool, new Type[] {});
	}
	
	@Override
	public Object call(Object[] args) {
		return Graph2D.unit.input.hasEvents();
	}

}
