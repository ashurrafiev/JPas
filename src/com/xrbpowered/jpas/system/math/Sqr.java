package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Sqr extends Function {

	private static Sqr intSqr = new Sqr(Type.integer) {
		public Object call(Object[] args) {
			int x = (Integer) args[0];
			return new Integer(x*x);
		};
	};

	private static Sqr realSqr = new Sqr(Type.real) {
		public Object call(Object[] args) {
			double x = (Double) args[0];
			return new Double(x*x);
		};
	};

	private final Type type;
	
	public Sqr() {
		this(null);
	}

	private Sqr(Type type) {
		this.type = type;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public Type[] getArgTypes() {
		return null;
	}

	@Override
	public Object call(Object[] args) {
		return null;
	}

	public Function.Call makeCall(Expression[] args) {
		testArgNumber(1, args);
		if(args[0].getType()==Type.integer)
			return new Function.Call(intSqr, args);
		else if(args[0].getType()==Type.real)
			return new Function.Call(realSqr, args);
		else
			throw new JPasError("Argument type mismatch");
	}
}
