package com.xrbpowered.jpas.units.graph2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.xrbpowered.jpas.units.graph2d.fonts.FontManager;

public class Target {

	public final BufferedImage buffer;
	public final Graphics2D gr;

	public boolean alpha = false;
	public Color penColor = Color.WHITE;
	public Paint paint = Color.WHITE;
	
	private LinkedList<AffineTransform> tfQueue = new LinkedList<>();

	public Target(BufferedImage buffer) {
		this.buffer = buffer;
		this.gr = (Graphics2D) buffer.getGraphics();
		this.gr.setFont(FontManager.getDefault());
	}
	
	public int getWidth() {
		return buffer.getWidth();
	}

	public int getHeight() {
		return buffer.getHeight();
	}

	public void clear() {
		gr.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
	}
	
	public Color makeColor(Object v) {
		return new Color((Integer) v, alpha);
	}
	
	public void tfReset() {
		gr.setTransform(new AffineTransform());
		tfQueue.clear();
	}
	
	public void tfPush() {
		tfQueue.add(gr.getTransform());
	}
	
	public void tfPop() {
		if(tfQueue.isEmpty())
			gr.setTransform(new AffineTransform());
		else
			gr.setTransform(tfQueue.removeLast());
	}

}
