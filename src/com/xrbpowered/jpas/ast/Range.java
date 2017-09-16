package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.data.Type.Ordinator;
import com.xrbpowered.jpas.ast.exp.Expression;

public class Range {

	public static class Fixed {
		public final Type type;
		public final int min, max;
		
		public Fixed(Type type, int min, int max) {
			this.type = type;
			this.min = min;
			this.max = max;
		}
		
		public int length() {
			return (max-min)+1;
		}
	}
	
	private final Expression min, max;
	
	public Range(Expression min, Expression max) {
		this.min = min;
		this.max = max;
	}
	
	public Type getType() {
		return min.getType();
	}
	
	public Fixed fix() {
		Object minv = min.evaluate();
		Object maxv = max.evaluate();
		Ordinator o = min.getType().getOrdinator();
		Fixed fixed = new Fixed(min.getType(), o.ord(minv), o.ord(maxv));
		if(fixed.length()<1)
			throw new JPasError("Invalid range.");
		return fixed;
	}
	
	public static Range make(Expression min, Expression max) {
		Type mint = min.getType();
		Type maxt = max.getType();
		if(mint.getOrdinator()==null || maxt.getOrdinator()==null)
			throw new JPasError("Range requires ordinal type.");
		if(!mint.equals(maxt))
			throw new JPasError("Range type mismatch.");
		return new Range(min, max);
	}
	
}
