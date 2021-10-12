package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;

public class FPSCount extends StdFunction {

	private static long start = 0L;
	private static int count = 0;
	private static double fps = 0.0;
	
	public FPSCount() {
		super(Type.real, new Type[] {});
	}
	
	@Override
	public Object call(Object[] args) {
		long t = System.currentTimeMillis();
		if(t-start>100) {
			fps = count / (double) ((t-start) /1000.0);
			start = t;
			count = 0;
		}
		count++;
		return fps;
	}

}
