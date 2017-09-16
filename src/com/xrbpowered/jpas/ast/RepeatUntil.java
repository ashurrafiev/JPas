package com.xrbpowered.jpas.ast;

import java.util.List;

import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.mem.StackFrameDesc;

public class RepeatUntil extends BlockStatement {

	private final Expression cond;
	
	public RepeatUntil(List<Statement> statements, StackFrameDesc sf, Expression cond) {
		super(statements, sf);
		this.cond = cond;
	}
	
	@Override
	public void execute() {
		enter();
		do {
			executeBody();
		} while(!((Boolean) cond.evaluate()));
		leave();
	}

}
