package com.xrbpowered.jpas.ast;

public class ExitStatement extends Statement {

	private final String label;
	
	public ExitStatement(String label) {
		this.label = label.toLowerCase();
	}
	
	@Override
	public String execute() {
		return label;
	}

}
