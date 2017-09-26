package com.xrbpowered.jpas.units.graph2d.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class InputManager {

	private class CharEvent {
		public final char ch;
		public final long time;
		
		public CharEvent(char ch, long time) {
			this.ch = ch;
			this.time = time;
		}
	}
	
	public int mouseX = 0;
	public int mouseY = 0;
	public boolean mouseLeft = false;
	public boolean mouseRight = false;
	
	private int bufferSize = 0;
	private LinkedList<CharEvent> keyBuffer = new LinkedList<>();
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
			if(e.isAltDown()) {
				if(e.getKeyCode()==KeyEvent.VK_MINUS) {
					Graph2D.unit.zoom(-1);
					return;
				}
				else if(e.getKeyCode()==KeyEvent.VK_EQUALS || e.getKeyCode()==KeyEvent.VK_PLUS) {
					Graph2D.unit.zoom(1);
					return;
				}
				else if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					Graph2D.unit.toggleFullscreen();
					return;
				}
			}
			if(e.getKeyCode()==KeyEvent.VK_ALT || e.getKeyCode()==KeyEvent.VK_ALT_GRAPH)
				return;
			
			char ch = e.getKeyChar();
			long t = e.getWhen();
			if(((int) ch)==0xffff) {
				keyBuffer.add(new CharEvent('\0', t));
				keyBuffer.add(new CharEvent((char) e.getKeyCode(), t));
				bufferSize += 2;
			}
			else {
				keyBuffer.add(new CharEvent(ch, t));
				bufferSize++;
			}
			checkBufferSize();
			pressedKeys.add(e.getKeyCode());
		}
		@Override
		public void keyReleased(KeyEvent e) {
			pressedKeys.remove(e.getKeyCode());
		}
	};
	
	public boolean isKeyDown(int code) {
		return pressedKeys.contains(code);
	}
	
	private void checkBufferSize() {
		while(bufferSize>32) {
			bufferSize--;
			if(keyBuffer.removeFirst().ch=='\0') {
				bufferSize--;
				keyBuffer.removeFirst();
			};
		}
	}
	
	private void removeOldEvents() {
		long t = System.currentTimeMillis() - 1000L;
		for(Iterator<CharEvent> i = keyBuffer.iterator(); i.hasNext();) {
			CharEvent e = i.next();
			if(e.time>t)
				return;
			i.remove();
		}
	}
	
	public boolean hasEvents() {
		removeOldEvents();
		return !keyBuffer.isEmpty();
	}
	
	public char getKey() {
		removeOldEvents();
		if(keyBuffer.isEmpty())
			return '\0';
		else {
			bufferSize--;
			return keyBuffer.removeFirst().ch;
		}
	}
	
	public static void register(Scope scope) {
		scope.add("MouseX", new MousePos(true));
		scope.add("MouseY", new MousePos(false));
		scope.add("LeftMouse", new MouseButtons(true));
		scope.add("RightMouse", new MouseButtons(false));
		scope.add("KeyDown", new KeyDown());
		scope.add("KeyPressed", new KeyPressed());
		scope.add("ReadKey", new ReadKey());
	}
	
}
