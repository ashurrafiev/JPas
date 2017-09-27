package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class FillPolygon extends StdProcedure {

	public FillPolygon() {
		// The array arguments are not LValues because:
		// 1) in this case the use can call the function with array literals, which is very handy
		// 2) the function is already very inefficient and should not be called in performance-critical sections anyway
		super(new Type[] {Type.integer, new ArrayType(null, Type.integer), new ArrayType(null, Type.integer)});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.gr.setPaint(t.paint);
		int n = (Integer) args[0];
		t.gr.fillPolygon(FillPolygon.getIntValues(n, (ArrayObject) args[1]), FillPolygon.getIntValues(n, (ArrayObject) args[2]), n);
		return null;
	}
	
	public static int[] getIntValues(int n, ArrayObject ar) {
		// This is very slow and inefficient, but I don't know the way around it
		Object[] values = ar.getValues();
		if(n>values.length)
			throw JPasError.rangeCheckError();
		int[] ints = new int[n];
		for(int i=0; i<n; i++)
			ints[i] = (Integer) values[i];
		return ints;
	}

}
