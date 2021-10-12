package com.xrbpowered.jpas.units.graph2d.bitmaps;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class UseWindow extends StdProcedure {

	public UseWindow() {
		super(new Type[] {});
	}

	@Override
	public Object call(Object[] args) {
		Graph2D.unit.setTarget(null);
		return null;
	}

}
