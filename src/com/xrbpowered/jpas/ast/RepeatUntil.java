package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.exp.Expression;

public class RepeatUntil extends Statement {

	private final Expression cond;
	private final Statement s;
	
	public RepeatUntil(Statement s, Expression cond) {
		this.cond = cond;
		this.s = s;
	}
	
	@Override
	public void execute() {
		do {
			s.execute();
		} while(!((Boolean) cond.evaluate()));
	}

}
