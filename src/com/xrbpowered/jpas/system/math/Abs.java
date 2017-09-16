package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Abs extends Function {

	private static Abs intAbs = new Abs(Type.integer) {
		public Object call(Object[] args) {
			return new Integer(Math.abs((Integer) args[0]));
		};
	};

	private static Abs realAbs = new Abs(Type.real) {
		public Object call(Object[] args) {
			return new Double(Math.abs((Double) args[0]));
		};
	};

	private final Type type;
	
	public Abs() {
		this(null);
	}

	private Abs(Type type) {
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
			return new Function.Call(intAbs, args);
		else if(args[0].getType()==Type.real)
			return new Function.Call(realAbs, args);
		else
			throw new JPasError("Argument type mismatch");
	}
}
