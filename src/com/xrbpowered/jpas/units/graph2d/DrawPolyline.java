package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class DrawPolyline extends StdProcedure {

	public DrawPolyline() {
		// The array arguments are not LValues because:
		// 1) in this case the use can call the function with array literals, which is very handy
		// 2) the function is already very inefficient and should not be called in performance-critical sections anyway
		super(new Type[] {Type.integer, new ArrayType(null, Type.integer), new ArrayType(null, Type.integer)});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.gr.setColor(t.penColor);
		int n = (Integer) args[0];
		t.gr.drawPolyline(FillPolygon.getIntValues(n, (ArrayObject) args[1]), FillPolygon.getIntValues(n, (ArrayObject) args[2]), n);
		return null;
	}
	
}
