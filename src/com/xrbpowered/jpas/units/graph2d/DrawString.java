package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class DrawString extends StdProcedure {

	public DrawString() {
		super(new Type[] {Type.integer, Type.integer, Type.string});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.gr.setColor(t.penColor);
		t.gr.drawString((String) args[2], (Integer) args[0], (Integer) args[1]);
		return null;
	}

}
