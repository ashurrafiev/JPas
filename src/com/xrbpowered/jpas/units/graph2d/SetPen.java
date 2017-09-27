package com.xrbpowered.jpas.units.graph2d;

import java.awt.BasicStroke;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class SetPen extends StdProcedure {

	public SetPen() {
		super(new Type[] {Type.integer, Type.real});
	}

	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.penColor = t.makeColor(args[0]);
		float w = new Float((Double) args[1]);
		if(w<0f)
			throw JPasError.rangeCheckError();
		t.gr.setStroke(new BasicStroke(w));
		return null;
	}

}
