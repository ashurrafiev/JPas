package com.xrbpowered.jpas.units.graph2d.bitmaps;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;
import com.xrbpowered.jpas.units.graph2d.Graph2D;
import com.xrbpowered.jpas.units.graph2d.Target;

public class PutPixel extends StdProcedure {

	public PutPixel() {
		super(new Type[] {Type.integer, Type.integer, Type.integer});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		try {
			t.buffer.setRGB((Integer) args[0], (Integer) args[1], (Integer) args[2]);
		}
		catch(ArrayIndexOutOfBoundsException e) {
		}
		return null;
	}

}
