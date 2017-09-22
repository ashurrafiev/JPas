package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class SetBackground extends StdProcedure {

	public SetBackground() {
		super(new Type[] {Type.integer});
	}

	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.gr.setBackground(t.makeColor(args[0]));
		return null;
	}

}
