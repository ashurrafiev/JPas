package com.xrbpowered.jpas.units.graph2d.transform;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;
import com.xrbpowered.jpas.units.graph2d.Graph2D;
import com.xrbpowered.jpas.units.graph2d.Target;

public class TFTranslate extends StdProcedure {

	public TFTranslate() {
		super(new Type[] {Type.real, Type.real});
	}

	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.gr.translate((Double) args[0], (Double) args[1]);
		return null;
	}

}
