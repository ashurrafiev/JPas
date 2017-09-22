package com.xrbpowered.jpas.units.graph2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.units.StandardUnit;
import com.xrbpowered.jpas.units.graph2d.fonts.SetTextFont;
import com.xrbpowered.jpas.units.graph2d.fonts.SetTextSize;
import com.xrbpowered.jpas.units.graph2d.input.InputManager;

public class Graph2D extends StandardUnit {

	private class WindowPane extends JPanel {
		public WindowPane(int width, int height) {
			setPreferredSize(new Dimension(width, height));
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);
			addMouseListener(input.mouseListener);
			addMouseMotionListener(input.mouseListener);
			addKeyListener(input.keyListener);
		}
		
		@Override
		public void paint(java.awt.Graphics g) {
			int sw = getWidth();
			int sh = getHeight();
			int w = screenBuffer.getWidth() * Graph2D.this.pixelScale;
			int h = screenBuffer.getHeight() * Graph2D.this.pixelScale;
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, sw, sh);
			g2.drawImage(screenBuffer, (sw-w)/2, (sh-h)/2, w, h, null);
		}
	}
	
	private JFrame window = null;
	private Runnable presenter = null;
	private int pixelScale;
	
	public final InputManager input = new InputManager();
	
	private BufferedImage screenBuffer = null;
	private Target target = null;
	
	public void setWindow(String title, int width, int height, int pixelScale) {
		if(width<1 || height<1)
			throw JPasError.rangeCheckError.get();
		if(window==null) {
			window = new JFrame(title.isEmpty() ? "JPas" : title);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			presenter = new Runnable() {
				@Override
				public void run() {
					window.repaint();
				}
			};
			
			screenBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			target = new Target(screenBuffer);
			this.pixelScale = pixelScale>0 ? pixelScale : 1;
			
			window.setContentPane(new WindowPane(width*pixelScale, height*pixelScale));
			window.setResizable(false);
			window.pack();
			window.setLocationRelativeTo(null);
			window.setVisible(true);
		}
		else
			throw new JPasError("Graphics already initialized.");
	}
	
	public void check() {
		if(target==null)
			throw new JPasError("Graphics not initialized.");
	}
	
	public Target getTarget() {
		check();
		return target;
	}
	
	public void present() {
		check();
		try {
			SwingUtilities.invokeAndWait(presenter);
		}
		catch(Exception e) {			
		}
	}
	
	@Override
	public Statement use(Scope scope) {
		scope.add("InitWindow", new InitWindow());
		scope.add("ClearScreen", new ClearScreen());
		scope.add("ScreenWidth", new ScreenSize(true));
		scope.add("ScreenHeight", new ScreenSize(false));
		scope.add("PresentScreen", new Present());

		scope.add("SetBackground", new SetBackground());
		scope.add("SetPen", new SetPen());
		scope.add("SetPaint", new SetPaint());
		scope.add("HighQuality", new SetQuality(true));
		scope.add("LowQuality", new SetQuality(false));
		scope.add("TransparencyOn", new SetAlpha(true));
		scope.add("TransparencyOff", new SetAlpha(false));
		scope.add("SetClip", new SetClip());
		scope.add("ResetClip", new ResetClip());
		
		scope.add("DrawArc", new DrawArc());
		scope.add("DrawLine", new DrawLine());
		scope.add("DrawOval", new DrawOval());
		scope.add("DrawPolygon", new DrawPolygon());
		scope.add("DrawPolyline", new DrawPolyline());
		scope.add("DrawRect", new DrawRect());
		scope.add("DrawRoundRect", new DrawRoundRect());
		scope.add("DrawText", new DrawString());
		scope.add("FillArc", new FillArc());
		scope.add("FillOval", new FillOval());
		scope.add("FillPolygon", new FillPolygon());
		scope.add("FillRect", new FillRect());
		scope.add("FillRoundRect", new FillRoundRect());
		
		scope.add("TextWidth", new TextWidth());
		scope.add("SetTextFont", new SetTextFont());
		scope.add("SetTextSize", new SetTextSize());
		
		InputManager.register(scope);

		// TODO transforms
		// TODO bitmaps
		// TODO toggle fullscreen
		
		return Statement.nop;
	}
	
	public static final Graph2D unit = new Graph2D();
	
}
