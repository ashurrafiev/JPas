package com.xrbpowered.jpas.units.graph2d.bitmaps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.units.StdFunction;
import com.xrbpowered.jpas.units.graph2d.Graph2D;
import com.xrbpowered.jpas.units.graph2d.Target;

public class GetBitmap extends StdFunction {

	public GetBitmap() {
		super(Type.integer, new Type[] {Type.integer, Type.integer, Type.integer, Type.integer});
	}

	@Override
	public Object call(Object[] args) {
		Target t = Graph2D.unit.getTarget();
		
		int x = (Integer) args[0];
		int y = (Integer) args[1];
		int w = (Integer) args[2];
		int h = (Integer) args[3];
		if(w<1 || h<1)
			return 0;
		
		BufferedImage image = new BufferedImage(w, h, t.buffer.getType());
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setBackground(new Color(0, true));
		g2.clearRect(0, 0, w, h);
		g2.drawImage(t.buffer, 0, 0, w, h, x, y, x+w, y+h, null);
		
		return BitmapManager.getInstance().createHandle(image);
	}

}
