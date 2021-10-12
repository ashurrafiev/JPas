package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.mem.Pointer;

public class Val extends Function {

	private static Val intVal = new Val() {
		public Object convert(String s, int code) {
			return Integer.parseInt(s, code);
		}
	};

	private static Val realVal = new Val() {
		public Object convert(String s, int code) {
			if(code!=10)
				throw new NumberFormatException();
			else
				return Double.parseDouble(s);
		}
	};
	
	@Override
	public EntryType getScopeEntryType() {
		return EntryType.procedure;
	}
	
	@Override
	public Type getType() {
		return null;
	}

	@Override
	public int getArgNum() {
		return 3;
	}

	@Override
	public Type getArgType(int argIndex) {
		return argIndex==0 ? Type.string : Type.integer;
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return argIndex>0;
	}

	public Object convert(String s, int code) {
		return null;
	}
	
	@Override
	public Object call(Object[] args) {
		String s = (String) args[0];
		Pointer codePtr = (Pointer) args[2];
		int code = (Integer) codePtr.read();
		try {
			Object out = convert(s, code);
			((Pointer) args[1]).write(out);
		}
		catch(NumberFormatException e) {
			code = 0;
		}
		codePtr.write(code);
		return null;
	}

	@Override
	public Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		args[0] = checkTypeCast(scope, getArgType(0, args), args[0]);
		args[2] = checkTypeCast(scope, getArgType(2, args), args[2]);
		args[1] = FunctionType.dereference(scope, args[1]);
		checkLValue(1, args[1]);
		checkLValue(2, args[2]);
		if(args[1].getType()==Type.integer)
			return new Function.Call(intVal, args);
		else if(args[1].getType()==Type.real)
			return new Function.Call(realVal, args);
		else
			throw JPasError.argumentTypeError();
	}
	
}
