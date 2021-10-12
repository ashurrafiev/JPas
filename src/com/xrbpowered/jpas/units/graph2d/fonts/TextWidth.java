package com.xrbpowered.jpas.units.graph2d.fonts;

import java.awt.FontMetrics;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.jpas.units.graph2d.Graph2D;
import com.xrbpowered.jpas.units.graph2d.Target;

public class TextWidth extends StdFunction {

	public TextWidth() {
		super(Type.integer, new Type[] {Type.string});
	}
	
	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		FontMetrics fm = t.gr.getFontMetrics();
		return fm.stringWidth((String) args[0]);
	}

}
