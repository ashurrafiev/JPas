package com.xrbpowered.jpas.units.graph2d;

import java.awt.FontMetrics;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;

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
