package com.xrbpowered.jpas.units.graph2d.bitmaps;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;

public class BitmapManager {

	private static BitmapManager instance = null;
	
	private HashMap<Integer, BufferedImage> cache = new HashMap<>();
	private int nextHandle = 1;
	
	private BitmapManager() {
	}
	
	public int createHandle(BufferedImage image) {
		int h = nextHandle;
		cache.put(h, image);
		nextHandle++;
		return h;
	}
	
	public BufferedImage get(int handle) {
		BufferedImage image = cache.get(handle);
		if(image==null)
			throw new JPasError("No bitmap");
		return image;
	}
	
	public static BitmapManager getInstance() {
		if(instance==null)
			instance = new BitmapManager();
		return instance;
	}
	
	public static void register(Scope scope) {
		scope.add("CreateBitmap", new CreateBitmap());
		scope.add("LoadBitmap", new LoadBitmap());
		scope.add("LoadAtlas", new LoadAtlas());

		scope.add("PutBitmap", new PutBitmap());
		scope.add("StretchBitmap", new StretchBitmap());

		scope.add("UseBitmap", new UseBitmap());
		scope.add("UseWindow", new UseWindow());

	}
}
