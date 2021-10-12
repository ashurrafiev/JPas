package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class ClearScreen extends StdProcedure {

	public ClearScreen() {
		super(new Type[] {});
	}
	
	@Override
	public Object call(Object[] args) {
		Graph2D.unit.getTarget().clear();
		return null;
	}

}
