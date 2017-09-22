package com.xrbpowered.jpas.units.graph2d.fonts;

import java.awt.Font;
import java.util.HashMap;

public class FontManager {

	private static final String defFamily = "Tahoma";
	private static final int defStyle = Font.PLAIN;
	private static final int defSize = 14;
	
	private static FontManager instance = null;
	
	private final Font def;
	private HashMap<String, Font> cache = new HashMap<>();
	
	private FontManager() {
		this.def = new Font(defFamily, defStyle, defSize);
		cache.put(defFamily.toLowerCase(), this.def);
	}
	
	public Font get(String name, int style) {
		Font font = cache.get(name);
		if(font==null) {
			font = new Font(name, style, defSize);
			cache.put(name, font);
		}
		else
			font = font.deriveFont(style);
		return font;
	}
	
	private static FontManager getInstance() {
		if(instance==null)
			instance = new FontManager();
		return instance;
	}
	
	public static Font getFont(String name, boolean bold, boolean italic) {
		int style = 0;
		if(bold) style += Font.BOLD;
		if(italic) style += Font.ITALIC;
		return getInstance().get(name, style);
	}
	
	public static Font getDefault() {
		return getInstance().def;
	}
	
}
