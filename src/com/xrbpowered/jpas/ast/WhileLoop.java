package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.exp.Expression;

public class WhileLoop extends Statement {

	private final Expression cond;
	private final Statement s;
	
	public WhileLoop(Expression cond, Statement s) {
		this.cond = cond;
		this.s = s;
	}
	
	@Override
	public void execute() {
		while((Boolean) cond.evaluate())
			s.execute();
	}

}
