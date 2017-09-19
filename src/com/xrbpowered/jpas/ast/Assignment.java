package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.LValue;

public class Assignment extends Statement {

	private final LValue target;
	private final Expression ex;
	
	public Assignment(LValue target, Expression ex) {
		this.target = target;
		this.ex = ex;
	}
	
	@Override
	public String execute() {
		target.assign(ex.evaluate());
		return null;
	}
}
