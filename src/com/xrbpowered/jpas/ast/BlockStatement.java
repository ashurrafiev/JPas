package com.xrbpowered.jpas.ast;

import java.util.ArrayList;

public class BlockStatement extends Statement {

	private ArrayList<Statement> statements = new ArrayList<>();
	
	public void addStatement(Statement s) {
		statements.add(s);
	}
	
	@Override
	public void execute() {
		for(Statement s : statements)
			s.execute();
		// TODO release scoped values
	}
}
