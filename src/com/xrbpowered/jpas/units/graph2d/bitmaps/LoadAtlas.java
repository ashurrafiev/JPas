package com.xrbpowered.jpas.units.graph2d.bitmaps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;
import com.xrbpowered.jpas.units.StdProcedure;

public class LoadAtlas extends StdProcedure {

	public LoadAtlas() {
		super(new Type[] {Type.string, Type.integer, Type.integer, new ArrayType(null, Type.integer), Type.integer});
	}

	@Override
	public boolean isLValue(int argIndex) {
		return argIndex==3;
	}
	
	@Override
	public Object call(Object[] args) {
		int w = (Integer) args[1];
		int h = (Integer) args[2];
		Object[] arv = ((ArrayObject) ((Pointer) args[3]).read()).getValues();
		int count = (Integer) args[4];
		if(count>arv.length)
			throw JPasError.rangeCheckError();
		
		BufferedImage image = LoadBitmap.loadImage(args);
		int sx = 0;
		int sy = 0;
		for(int i=0; i<count; i++) {
			BufferedImage tile = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) tile.getGraphics();
			g2.setBackground(new Color(0, true));
			g2.clearRect(0, 0, w, h);
			g2.drawImage(image, 0, 0, w, h, sx, sy, sx+w, sy+h, null);
			sx += w;
			if(sx>=image.getWidth()) {
				sx = 0;
				sy = h;
			}
			
			arv[i] = BitmapManager.getInstance().createHandle(tile);
		}
		return null;
	}

}
