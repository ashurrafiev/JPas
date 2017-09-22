package com.xrbpowered.jpas.units.graph2d.colors;

import java.awt.Color;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.utils.ColorUtils;
import com.xrbpowered.utils.MathUtils;

public class BlendColors extends StdFunction {

	public BlendColors() {
		super(Type.integer, new Type[] {Type.real, Type.integer, Type.integer});
	}
	
	@Override
	public boolean hasSideEffects() {
		return false;
	}
	
	@Override
	public Object call(Object[] args) {
		double s = MathUtils.snap((Double) args[0]);
		return new Integer(ColorUtils.blend(new Color((Integer) args[1], true), new Color((Integer) args[2], true), s).getRGB());
	}
}
