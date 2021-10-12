package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class SetPaint extends StdProcedure {

	public SetPaint() {
		super(new Type[] {Type.integer});
	}

	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.paint = t.makeColor(args[0]);
		return null;
	}

}
