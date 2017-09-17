package com.xrbpowered.jpas.ast;

public abstract class Statement {

	public abstract void execute();
	
	public static final Statement nop = new Statement() {
		@Override
		public void execute() {
		}
	};
	
}
