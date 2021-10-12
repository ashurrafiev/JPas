package com.xrbpowered.jpas.ast.data;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type.Ordinator;
import com.xrbpowered.jpas.ast.exp.Expression;

public class Range {
	public final Type type;
	public final int min, max;
	
	public Range(Type type, int min, int max) {
		this.type = type;
		this.min = min;
		this.max = max;
	}
	
	public int length() {
		return (max-min)+1;
	}
	
	public Object indexFor(int i) {
		return type.getOrdinator().unord(i+min);
	}

	public boolean check(Object obj) {
		int i = type.getOrdinator().ord(obj);
		return i>=min && i<=max;
	}
	
	public static Range make(Expression min, Expression max) {
		Type mint = min.getType();
		Type maxt = max.getType();
		if(mint.getOrdinator()==null || maxt.getOrdinator()==null)
			throw new JPasError("Range type error.");
		if(!mint.equals(maxt))
			throw new JPasError("Range type mismatch.");
		
		if(!min.isConst() || !max.isConst())
			throw new JPasError("Expression is not constant.");
		
		Object minv = min.evaluate();
		Object maxv = max.evaluate();
		Ordinator o = min.getType().getOrdinator();
		Range fixed = new Range(min.getType(), o.ord(minv), o.ord(maxv));
		if(fixed.length()<1)
			throw new JPasError("Invalid range.");
		return fixed;
	}

	public static boolean checkEqual(Range rx, Range ry) {
		Type tx = rx==null ? null : rx.type;
		Type ty = ry==null ? null : ry.type;
		if(tx!=null && ty!=null && !tx.equals(ty))
			return false;
		if(rx!=null && ry!=null)
			return rx.min==ry.min && rx.max==ry.max;
		else
			return true;
	}
}