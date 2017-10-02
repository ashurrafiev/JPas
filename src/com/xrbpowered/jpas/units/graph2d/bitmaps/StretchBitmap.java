package com.xrbpowered.jpas.units.graph2d.bitmaps;

import java.awt.image.BufferedImage;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdProcedure;
import com.xrbpowered.jpas.units.graph2d.Graph2D;
import com.xrbpowered.jpas.units.graph2d.Target;

public class StretchBitmap extends StdProcedure {

	public StretchBitmap() {
		super(new Type[] {Type.integer, Type.integer, Type.integer, Type.integer, Type.integer});
	}

	@Override
	public Object call(Object[] args) {
		BufferedImage image = BitmapManager.getInstance().get((Integer) args[4]);
		Target t = Graph2D.unit.getTarget();
		t.gr.drawImage(image, (Integer) args[0], (Integer) args[1], (Integer) args[2], (Integer) args[3], null);
		return null;
	}

}
