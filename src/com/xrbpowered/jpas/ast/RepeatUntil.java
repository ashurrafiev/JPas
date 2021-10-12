package com.xrbpowered.jpas.ast;

import java.util.List;

import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.mem.StackFrameDesc;

public class RepeatUntil extends BlockStatement {

	private final Expression cond;
	
	public RepeatUntil(String label, List<Statement> statements, StackFrameDesc sf, Expression cond) {
		super(label, statements, sf);
		this.cond = cond;
	}
	
	@Override
	public String execute() {
		String exit = null;
		enter();
		do {
			exit = executeBody();
			if(exit!=null)
				break;
		} while(!((Boolean) cond.evaluate()));
		leave();
		return pass(exit);
	}

}
