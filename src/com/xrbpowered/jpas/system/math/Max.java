package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Max extends Function {

	private static Max intMax = new Max(Type.integer) {
		public Object call(Object[] args) {
			return new Integer(Math.max((Integer) args[0], (Integer) args[1]));
		};
	};

	private static Max realMax = new Max(Type.real) {
		public Object call(Object[] args) {
			return new Double(Math.max((Double) args[0], (Double) args[1]));
		};
	};

	private final Type type;
	
	public Max() {
		this(null);
	}

	private Max(Type type) {
		this.type = type;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public int getArgNum() {
		return 2;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return null;
	}


	@Override
	public Object call(Object[] args) {
		return null;
	}

	public Function.Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(2, args);
		args[0] = FunctionType.dereference(scope, args[0]);
		args[1] = FunctionType.dereference(scope, args[1]);
		Type xt = args[0].getType();
		Type yt = args[1].getType();
		if(xt==Type.integer && yt==Type.integer)
			return new Function.Call(intMax, args);
		else if(xt==Type.real && yt==Type.integer || yt==Type.real && xt==Type.integer || xt==Type.real && yt==Type.real) {
			args[0] = checkTypeCast(scope, Type.real, args[0]);
			args[1] = checkTypeCast(scope, Type.real, args[1]);
			return new Function.Call(realMax, args);
		}
		else
			throw JPasError.argumentTypeError();
	}
}
