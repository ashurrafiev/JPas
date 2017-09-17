package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Rand extends Function {

	private static Rand intRand = new Rand(Type.integer) {
		public Object call(Object[] args) {
			return new Integer(Randomize.random.nextInt((Integer) args[0]));
		};
	};

	private static Rand realRand = new Rand(Type.real) {
		public Object call(Object[] args) {
			return new Double(Randomize.random.nextDouble());
		};
	};

	private final Type type;
	
	public Rand() {
		this(null);
	}
	
	private Rand(Type type) {
		this.type = type;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public int getArgNum() {
		return 0;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return null;
	}


	@Override
	public boolean hasSideEffects() {
		return true;
	}

	@Override
	public Object call(Object[] args) {
		return null;
	}

	public Function.Call makeCall(Expression[] args) {
		if(args==null || args.length==0)
			return new Function.Call(realRand, null);
		else {
			testArgNumber(1, args);
			if(args[0].getType()==Type.integer)
				return new Function.Call(intRand, args);
			else
				throw new JPasError("Argument type mismatch");
		}
	}

}
