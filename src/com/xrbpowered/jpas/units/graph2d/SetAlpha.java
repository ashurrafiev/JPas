package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class SetAlpha extends StdProcedure {

	private final boolean alpha;
	
	public SetAlpha(boolean alpha) {
		super(new Type[] {});
		this.alpha = alpha;
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.alpha = alpha;
		return null;
	}

}
