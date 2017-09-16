package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.Type.Ordinator;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Variable;

public class ForLoop extends Statement {

	private int dir;
	private Variable var; 
	private final Expression start, end;
	private final Statement s;
	
	public ForLoop(int dir, Variable var, Expression start, Expression end, Statement s) {
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
		if(dir==0 && i>endi || dir!=0 && i<endi)
			return;
		for(;;) {
			s.execute();
			
			i = ord.ord(var.evaluate());
			endi = ord.ord(end.evaluate());
			
			// pred/succ may throw range error, need to check condition prior that
			if(dir==0 && i>=endi || dir!=0 && i<=endi)
				return;
			var.assign(dir==0 ? ord.succ(var.evaluate()) : ord.pred(var.evaluate()));
		}
	}

}
