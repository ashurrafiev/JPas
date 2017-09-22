package com.xrbpowered.jpas.units.graph2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Target {

	public final BufferedImage buffer;
	public final Graphics2D gr;

	public boolean alpha = false;
	public Color penColor = Color.WHITE;
	public Color paintColor = Color.WHITE;

	public Target(BufferedImage buffer) {
		this.buffer = buffer;
		this.gr = (Graphics2D) buffer.getGraphics();
		this.gr.setFont(new Font("Tahoma", Font.BOLD, 13));
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

}
