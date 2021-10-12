package com.xrbpowered.jpas.ast;

import java.util.ArrayList;
import java.util.List;

import com.xrbpowered.jpas.mem.StackFrameDesc;

public class BlockStatement extends LabelledStatement {

	protected final StackFrameDesc sf;
	protected final List<Statement> statements;
	
	public BlockStatement(String label, List<Statement> statements, StackFrameDesc sf) {
		super(label);
		this.statements = statements;
		this.sf = sf==null || sf.size()==0 ? null : sf;
	}
	
	protected void enter() {
		if(sf!=null)
			sf.alloc();
	}
	
	protected String executeBody() {
		String exit = null;
		for(Statement s : statements) {
			exit = s.execute();
			if(exit!=null)
				break;
		}
		return exit;
	}
	
	protected void leave() {
		if(sf!=null)
			sf.release();
	}
	
	@Override
	public String execute() {
		enter();
		String exit = executeBody();
		leave();
		return pass(exit);
	}
	
	public static Statement wrap(Statement s, StackFrameDesc sf) {
		List<Statement> statements = new ArrayList<>(1);
		statements.add(s);
		return new BlockStatement(null, statements, sf);
	}
}
