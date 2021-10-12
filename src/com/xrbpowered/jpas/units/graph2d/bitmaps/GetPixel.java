package com.xrbpowered.jpas.units.graph2d.bitmaps;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.jpas.units.graph2d.Graph2D;
import com.xrbpowered.jpas.units.graph2d.Target;

public class GetPixel extends StdFunction {

	public GetPixel() {
		super(Type.integer, new Type[] {Type.integer, Type.integer});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		try {
			return t.buffer.getRGB((Integer) args[0], (Integer) args[1]);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

}
