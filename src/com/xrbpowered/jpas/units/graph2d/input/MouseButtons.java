package com.xrbpowered.jpas.units.graph2d.input;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class MouseButtons extends StdFunction {

	private final boolean left;
	
	public MouseButtons(boolean left) {
		super(Type.bool, new Type[] {});
		this.left = left;
	}
	
	@Override
	public Object call(Object[] args) {
		if(left)
			return Graph2D.unit.input.mouseLeft;
		else
			return Graph2D.unit.input.mouseRight;
	}

}
