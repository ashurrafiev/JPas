package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.data.Type.Ordinator;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.LValue;

public class ForLoop extends LabelledStatement {

	protected int dir;
	protected LValue var; 
	protected final Expression start, end;
	protected final Statement s;
	
	private ForLoop(String label, int dir, LValue var, Expression start, Expression end, Statement s) {
		super(label);
		this.dir = dir;
		this.var = var;
		this.start = start;
		this.end = end;
		this.s = s;
	}
	
	@Override
	public String execute() {
		String exit = null;
		Ordinator ord = var.getType().getOrdinator();
		var.assign(start.evaluate());
		int i = ord.ord(var.evaluate());
		int endi = ord.ord(end.evaluate());
		if(i*dir>endi*dir)
			return null;
		for(;;) {
			exit = s.execute();
			if(exit!=null)
				break;
			
			// pred/succ may overflow the range, need to check condition prior that
			i = ord.ord(var.evaluate()) + dir;
			if(i*dir>endi*dir)
				return null;
			var.assign(ord.unord(i));
		}
		return pass(exit);
	}
	
	public static ForLoop make(String label, int dir, LValue var, Expression start, Expression end, Statement s) {
		dir = dir==0 ? 1 : -1;
		return new ForLoop(label, dir, var, start, end, s);
	}

}
