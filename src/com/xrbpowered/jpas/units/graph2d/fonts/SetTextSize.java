package com.xrbpowered.jpas.units.graph2d.fonts;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;
import com.xrbpowered.jpas.units.graph2d.Graph2D;
import com.xrbpowered.jpas.units.graph2d.Target;

public class SetTextSize extends StdProcedure {

	public SetTextSize() {
		super(new Type[] {Type.integer});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		float size = (Integer) args[0];
		t.gr.setFont(t.gr.getFont().deriveFont(size));
		return null;
	}

}
