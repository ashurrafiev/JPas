package com.xrbpowered.jpas.ast;

import java.util.ArrayList;
import java.util.List;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.exp.Expression;

public class CaseStatement extends Statement {

	private static class Switch {
		public final Object val;
		public final Statement s;
		
		public Switch(Object val, Statement s) {
			this.val = val;
			this.s = s;
		}
	}
	
	private final Expression test;
	private List<Switch> switches = new ArrayList<>();
	private Statement def = null;
	
	public CaseStatement(Expression test) {
		this.test = test;
	}
	
	public void addSwitch(Object val, Statement s) {
		if(val==null)
			return;
		for(Switch sw : switches) {
			if(sw.val.equals(val))
				throw new JPasError("Dupicate case.");
		}
		switches.add(new Switch(val, s));
	}
	
	public void addElse(Statement s) {
		this.def = s;
	}
	
	@Override
	public String execute() {
		Object v = test.evaluate();
		if(v!=null) {
			for(Switch sw : switches) {
				if(sw.val.equals(v))
					return sw.s.execute();
			}
		}
		if(def!=null)
			return def.execute();
		else
			return null;
	}
	
}
