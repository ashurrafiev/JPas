package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class FillRoundRect extends StdProcedure {

	public FillRoundRect() {
		super(new Type[] {Type.integer, Type.integer, Type.integer, Type.integer, Type.integer});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.gr.setColor(t.paintColor);
		int r = (Integer) args[4];
		if(r<0) r = 0;
		t.gr.fillRoundRect((Integer) args[0], (Integer) args[1], (Integer) args[2], (Integer) args[3], r, r);
		return null;
	}

}
