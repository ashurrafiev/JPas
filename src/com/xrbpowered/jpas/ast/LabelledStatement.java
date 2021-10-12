package com.xrbpowered.jpas.ast;

public abstract class LabelledStatement extends Statement {

	public final String label;
	
	public LabelledStatement(String label) {
		this.label = label;
	}
	
	protected String pass(String exit) {
		if(exit==null || label!=null && label.equals(exit))
			return null;
		else
			return exit;
	}

}
