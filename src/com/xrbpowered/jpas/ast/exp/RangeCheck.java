package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Range;
import com.xrbpowered.jpas.ast.data.Type;

public class RangeCheck extends Expression {

	private final Range range;
	private final Expression ex;
	
	public RangeCheck(Range range, Expression ex) {
		this.range = range;
		this.ex = ex;
	}
	
	@Override
	public Type getType() {
		return ex.getType();
	}

	@Override
	public Object evaluate() {
		Object obj = ex.evaluate();
		if(!range.check(obj))
			JPasError.rangeCheckError();
		return obj;
	}

	@Override
	public boolean isConst() {
		return ex.isConst();
	}

}
