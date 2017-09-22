package com.xrbpowered.jpas.units.graph2d.input;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class ReadKey extends StdFunction {

	private final boolean code;
	
	public ReadKey(boolean code) {
		super(code ? Type.integer : Type.character, new Type[] {});
		this.code = code;
	}
	
	@Override
	public Object call(Object[] args) {
		if(code)
			return Graph2D.unit.input.getKeyCode();
		else
			return Graph2D.unit.input.getKeyChar();
	}

}
