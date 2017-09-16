package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.exp.Expression;

public class IfStatement extends Statement {

	private final Expression cond;
	private final Statement st, sf;
	
	public IfStatement(Expression cond, Statement st, Statement sf) {
		this.cond = cond;
		this.st = st;
		this.sf = sf;
	}
	
	@Override
	public void execute() {
		if((Boolean) cond.evaluate())
			st.execute();
		else if(sf!=null)
			sf.execute();
	}

}
