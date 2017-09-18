package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.data.Type.Ordinator;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.LValue;

public class ForLoop extends Statement {

	private static class Int extends ForLoop {
		public Int(int dir, LValue var, Expression start, Expression end, Statement s) {
			super(dir, var, start, end, s);
		}
			
		@Override
		public void execute() {
			var.assign(start.evaluate());
			int d = dir==0 ? 1 : -1;
			for(;;) {
				int i = (Integer) var.evaluate();
				int endi = (Integer) end.evaluate();
				if(dir==0 && i>endi || dir!=0 && i<endi)
					return;
				s.execute();
				var.assign((Integer) var.evaluate() + d);
			}
		}
	}
	
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
	
	public static ForLoop make(int dir, LValue var, Expression start, Expression end, Statement s) {
		if(var.getType()==Type.integer)
			return new Int(dir, var, start, end, s);
		else
			return new ForLoop(dir, var, start, end, s);
	}

}
