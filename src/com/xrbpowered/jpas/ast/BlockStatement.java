package com.xrbpowered.jpas.ast;

import java.util.List;

import com.xrbpowered.jpas.mem.StackFrameDesc;

public class BlockStatement extends Statement {

	protected final StackFrameDesc sf;
	protected final List<Statement> statements;
	
	public BlockStatement(List<Statement> statements, StackFrameDesc sf) {
		this.statements = statements;
		this.sf = sf==null || sf.size()==0 ? null : sf;
	}
	
	protected void enter() {
		if(sf!=null)
			sf.alloc();
	}
	
	protected void executeBody() {
		for(Statement s : statements)
			s.execute();
	}
	
	protected void leave() {
		if(sf!=null)
			sf.release();
	}
	
	@Override
	public void execute() {
		enter();
		executeBody();
		leave();
	}
}
