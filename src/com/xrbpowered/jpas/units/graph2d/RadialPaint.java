package com.xrbpowered.jpas.units.graph2d;

import java.awt.Color;
import java.awt.RadialGradientPaint;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class RadialPaint extends StdProcedure {

	public RadialPaint() {
		super(new Type[] {Type.integer, Type.integer, Type.integer, Type.integer, Type.integer});
	}

	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		int x = (Integer) args[0];
		int y = (Integer) args[1];
		int r = (Integer) args[2];
		Color c1 = t.makeColor(args[3]);
		Color c2 = t.makeColor(args[4]);
		t.paint = new RadialGradientPaint(x, y, r, new float[] {0f, 1f}, new Color[] {c1, c2});
		return null;
	}

}
