package com.xrbpowered.jpas.units.graph2d;

import java.awt.Color;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class GradientPaint extends StdProcedure {

	public GradientPaint() {
		super(new Type[] {Type.integer, Type.integer, Type.integer, Type.integer, Type.integer, Type.integer});
	}

	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		int x1 = (Integer) args[0];
		int y1 = (Integer) args[1];
		int x2 = (Integer) args[2];
		int y2 = (Integer) args[3];
		Color c1 = t.makeColor(args[4]);
		Color c2 = t.makeColor(args[5]);
		t.paint = new java.awt.GradientPaint(x1, y1, c1, x2, y2, c2);
		return null;
	}

}
