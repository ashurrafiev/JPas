package com.xrbpowered.jpas.units.graph2d;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class Present extends StdProcedure {

	public Present() {
		super(new Type[] {});
	}
	
	@Override
	public Object call(Object[] args) {
		Graph2D.unit.present();
		return null;
	}

}
