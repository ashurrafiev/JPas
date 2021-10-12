package com.xrbpowered.jpas.units.graph2d.transform;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class TFReset extends StdProcedure {

	public TFReset() {
		super(new Type[] {});
	}

	@Override
	public Object call(Object[] args) {
		Graph2D.unit.getTarget().tfReset();
		return null;
	}

}
