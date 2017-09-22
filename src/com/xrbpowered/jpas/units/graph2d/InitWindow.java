package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class InitWindow extends StdProcedure {

	public InitWindow() {
		super(new Type[] {Type.string, Type.integer, Type.integer, Type.integer});
	}
	
	@Override
	public Object call(Object[] args) {
		Graph2D.unit.setWindow((String) args[0], (Integer) args[1], (Integer) args[2], (Integer) args[3]);
		return null;
	}

}
