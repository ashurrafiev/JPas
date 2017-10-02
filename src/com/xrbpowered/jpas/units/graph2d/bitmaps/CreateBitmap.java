package com.xrbpowered.jpas.units.graph2d.bitmaps;

import java.awt.image.BufferedImage;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;

public class CreateBitmap extends StdFunction {

	public CreateBitmap() {
		super(Type.integer, new Type[] {Type.integer, Type.integer, Type.bool});
	}

	@Override
	public Object call(Object[] args) {
		boolean alpha = (Boolean) args[2];
		BufferedImage image = new BufferedImage((Integer) args[0], (Integer) args[1],
				alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		return BitmapManager.getInstance().createHandle(image);
	}

}
