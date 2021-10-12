package com.xrbpowered.jpas.units.graph2d.colors;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.utils.MathUtils;

public class Interpolate extends StdFunction {

	public Interpolate() {
		super(Type.real, new Type[] {Type.real, Type.real, Type.real});
	}
	
	@Override
	public boolean hasSideEffects() {
		return false;
	}
	
	@Override
	public Object call(Object[] args) {
		double s = MathUtils.snap((Double) args[0]);
		return MathUtils.lerp((Double) args[1], (Double) args[2], s);
	}

}
