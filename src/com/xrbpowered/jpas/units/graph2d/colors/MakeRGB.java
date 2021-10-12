package com.xrbpowered.jpas.units.graph2d.colors;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.utils.MathUtils;

public class MakeRGB extends StdFunction {

	public MakeRGB() {
		super(Type.integer, new Type[] {Type.real, Type.real, Type.real, Type.real});
	}
	
	@Override
	public boolean hasSideEffects() {
		return false;
	}
	
	@Override
	public Object call(Object[] args) {
		int r = MathUtils.snap((int)((Double) args[0] * 255.0), 0, 255);
		int g = MathUtils.snap((int)((Double) args[1] * 255.0), 0, 255);
		int b = MathUtils.snap((int)((Double) args[2] * 255.0), 0, 255);
		int a = MathUtils.snap((int)((Double) args[3] * 255.0), 0, 255);
		return new Integer((a<<24)|(r<<16)|(g<<8)|b);
	}

}
