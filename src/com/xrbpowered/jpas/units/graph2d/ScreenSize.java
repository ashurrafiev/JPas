package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;

public class ScreenSize extends StdFunction {

	private final boolean width;
	
	public ScreenSize(boolean width) {
		super(Type.integer, new Type[] {});
		this.width = width;
	}
	
	@Override
	public Object call(Object[] args) {
		if(width)
			return Graph2D.unit.getTarget().getWidth();
		else
			return Graph2D.unit.getTarget().getHeight();
	}

}
