package com.xrbpowered.jpas.ast;

public abstract class Statement {

	public abstract String execute();
	
	public static final Statement nop = new Statement() {
		@Override
		public String execute() {
			return null;
		}
	};
	
}
