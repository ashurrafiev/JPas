package com.xrbpowered.jpas.units.graph2d.transform;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class TFPush extends StdProcedure {

	private final boolean push;
	
	public TFPush(boolean push) {
		super(new Type[] {});
		this.push = push;
	}

	@Override
	public Object call(Object[] args) {
		if(push)
			Graph2D.unit.getTarget().tfPush();
		else
			Graph2D.unit.getTarget().tfPop();
		return null;
	}

}
