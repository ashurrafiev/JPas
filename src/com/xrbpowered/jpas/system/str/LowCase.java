package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class LowCase extends Function {

	private static LowCase charLowCase = new LowCase(Type.character) {
		public Object call(Object[] args) {
			return new Character(Character.toLowerCase((Character) args[0]));
		};
	};

	private static LowCase strLowCase = new LowCase(Type.string) {
		public Object call(Object[] args) {
			return ((String) args[0]).toLowerCase();
		};
	};

	private final Type type;
	
	public LowCase() {
		this(null);
	}

	private LowCase(Type type) {
		this.type = type;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public int getArgNum() {
		return 1;
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
		testArgNumber(1, args);
		args[0] = FunctionType.dereference(scope, args[0]);
		if(args[0].getType()==Type.character)
			return new Function.Call(charLowCase, args);
		else if(args[0].getType()==Type.string)
			return new Function.Call(strLowCase, args);
		else
			throw JPasError.argumentTypeError();
	}
	
}
