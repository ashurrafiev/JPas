package com.xrbpowered.jpas.units.graph2d.input;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class MousePos extends StdFunction {

	private final boolean x;
	
	public MousePos(boolean x) {
		super(Type.integer, new Type[] {});
		this.x = x;
	}
	
	@Override
	public Object call(Object[] args) {
		if(x)
			return Graph2D.unit.input.mouseX;
		else
			return Graph2D.unit.input.mouseY;
	}

}
