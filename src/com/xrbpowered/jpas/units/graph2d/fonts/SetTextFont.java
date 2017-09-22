package com.xrbpowered.jpas.units.graph2d.fonts;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;
import com.xrbpowered.jpas.units.graph2d.Graph2D;
import com.xrbpowered.jpas.units.graph2d.Target;

public class SetTextFont extends StdProcedure {

	public SetTextFont() {
		super(new Type[] {Type.string, Type.bool, Type.bool});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		t.gr.setFont(FontManager.getFont((String) args[0], (Boolean) args[1], (Boolean) args[2]));
		return null;
	}

}
