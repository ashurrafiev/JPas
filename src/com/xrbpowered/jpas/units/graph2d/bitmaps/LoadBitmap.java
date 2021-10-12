package com.xrbpowered.jpas.units.graph2d.bitmaps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xrbpowered.jpas.JPas;
import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;

public class LoadBitmap extends StdFunction {

	public LoadBitmap() {
		super(Type.integer, new Type[] {Type.string});
	}

	@Override
	public Object call(Object[] args) {
		BufferedImage image = loadImage(args);
		return BitmapManager.getInstance().createHandle(image);
	}

	public static BufferedImage loadImage(Object[] args) {
		String path = (String) args[0];
		try {
			return ImageIO.read(new File(JPas.workingDir, path));
		}
		catch(IOException e) {
			throw new JPasError("Unable to load bitmap");
		}
	}
	
}
