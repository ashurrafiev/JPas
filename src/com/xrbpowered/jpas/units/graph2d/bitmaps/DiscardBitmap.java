package com.xrbpowered.jpas.units.graph2d.bitmaps;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;

public class DiscardBitmap extends StdProcedure {

	public DiscardBitmap() {
		super(new Type[] {Type.integer});
	}

	@Override
	public Object call(Object[] args) {
		BitmapManager.getInstance().discard((Integer) args[2]);
		return null;
	}

}
