package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class FillRect extends StdProcedure {

	public FillRect() {
		super(new Type[] {Type.integer, Type.integer, Type.integer, Type.integer});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.gr.setPaint(t.paint);
		t.gr.fillRect((Integer) args[0], (Integer) args[1], (Integer) args[2], (Integer) args[3]);
		return null;
	}

}
