package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.exp.Expression;

public class WhileLoop extends LabelledStatement {

	private final Expression cond;
	private final Statement s;
	
	public WhileLoop(String label, Expression cond, Statement s) {
		super(label);
		this.cond = cond;
		this.s = s;
	}
	
	@Override
	public String execute() {
		String exit = null;
		while((Boolean) cond.evaluate()) {
			exit = s.execute();
			if(exit!=null)
				break;
		}
		return pass(exit);
	}

}
