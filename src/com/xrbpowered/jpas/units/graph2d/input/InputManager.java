package com.xrbpowered.jpas.units.graph2d.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.LinkedList;

import com.xrbpowered.jpas.ast.Scope;

public class InputManager {

	public int mouseX = 0;
	public int mouseY = 0;
	public boolean mouseLeft = false;
	public boolean mouseRight = false;
	
	private LinkedList<KeyEvent> keyEvents = new LinkedList<>();
	private HashSet<Integer> pressedKeys = new HashSet<>();
	
	public final MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton()==MouseEvent.BUTTON1)
				mouseLeft = true;
			else if(e.getButton()==MouseEvent.BUTTON3)
				mouseRight = true;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getButton()==MouseEvent.BUTTON1)
				mouseLeft = false;
			else if(e.getButton()==MouseEvent.BUTTON3)
				mouseRight = false;
		}
		@Override
		public void mouseMoved(java.awt.event.MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		};
		@Override
		public void mouseDragged(java.awt.event.MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		};
	};
	
	public final KeyAdapter keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			pressedKeys.add(e.getKeyCode());
		}
		@Override
		public void keyTyped(KeyEvent e) {
			keyEvents.add(e);
		};
		@Override
		public void keyReleased(KeyEvent e) {
			pressedKeys.remove(e.getKeyCode());
		}
	};
	
	public boolean isKeyDown(int code) {
		return pressedKeys.contains(code);
	}
	
	public boolean hasEvents() {
		return !keyEvents.isEmpty();
	}
	
	public char getKeyChar() {
		if(keyEvents.isEmpty())
			return '\0';
		else
			return keyEvents.removeLast().getKeyChar();
	}
	
	public int getKeyCode() {
		if(keyEvents.isEmpty())
			return 0;
		else
			return keyEvents.removeLast().getKeyCode();
	}
	
	public static void register(Scope scope) {
		scope.add("MouseX", new MousePos(true));
		scope.add("MouseY", new MousePos(false));
		scope.add("LeftMouse", new MouseButtons(true));
		scope.add("RightMouse", new MouseButtons(false));
		scope.add("KeyDown", new KeyDown());
		scope.add("KeyPressed", new KeyPressed());
		scope.add("ReadKeyCode", new ReadKey(true));
		scope.add("ReadKey", new ReadKey(false));
	}
	
}
