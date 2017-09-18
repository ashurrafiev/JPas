package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.data.Type.Ordinator;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.LValue;

public class ForLoop extends Statement {

	protected int dir;
	protected LValue var; 
	protected final Expression start, end;
	protected final Statement s;
	
	private ForLoop(int dir, LValue var, Expression start, Expression end, Statement s) {
		this.dir = dir;
		this.var = var;
		this.start = start;
		this.end = end;
		this.s = s;
	}
	
	@Override
	public void execute() {
		Ordinator ord = var.getType().getOrdinator();
		var.assign(start.evaluate());
		int i = ord.ord(var.evaluate());
		int endi = ord.ord(end.evaluate());
		if(i*dir>endi*dir)
			return;
		for(;;) {
			s.execute();
			
			// pred/succ may overflow the range, need to check condition prior that
			i = ord.ord(var.evaluate()) + dir;
			if(i*dir>endi*dir)
				return;
			var.assign(ord.unord(i));
		}
	}
	
	public static ForLoop make(int dir, LValue var, Expression start, Expression end, Statement s) {
		dir = dir==0 ? 1 : -1;
		return new ForLoop(dir, var, start, end, s);
	}

}
