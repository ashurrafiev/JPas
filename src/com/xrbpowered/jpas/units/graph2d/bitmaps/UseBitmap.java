package com.xrbpowered.jpas.units.graph2d.bitmaps;

import java.awt.image.BufferedImage;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class UseBitmap extends StdProcedure {

	public UseBitmap() {
		super(new Type[] {Type.integer});
	}

	@Override
	public Object call(Object[] args) {
		BufferedImage image = BitmapManager.getInstance().get((Integer) args[0]);
		Graph2D.unit.setTarget(image);
		return null;
	}

}
