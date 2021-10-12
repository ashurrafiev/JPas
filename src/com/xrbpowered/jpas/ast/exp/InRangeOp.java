package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.data.Range;
import com.xrbpowered.jpas.ast.data.Type;

public class InRangeOp extends Expression {

	private final Range range;
	private final Expression ex;
	
	public InRangeOp(Range range, Expression ex) {
		this.range = range;
		this.ex = ex;
	}
	
	@Override
	public Type getType() {
		return Type.bool;
	}

	@Override
	public Object evaluate() {
		Object obj = ex.evaluate();
		return range.check(obj);
	}

	@Override
	public boolean isConst() {
		return ex.isConst();
	}

}
