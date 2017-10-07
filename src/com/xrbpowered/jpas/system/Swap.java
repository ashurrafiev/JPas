package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.mem.Pointer;

public class Swap extends Function {

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
		return 2;
	}

	@Override
	public boolean isLValue(int argIndex) {
		return true;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return null;
	}

	@Override
	public Object call(Object[] args) {
		Pointer p0 = (Pointer) args[0];
		Pointer p1 = (Pointer) args[1];
		Object v = p0.read();
		p0.write(p1.read());
		p1.write(v);
		return null;
	}
	
	@Override
	public Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		checkLValue(0, args[0]);
		checkLValue(1, args[1]);
		if(!args[0].getType().equals(args[1].getType()))
			throw JPasError.argumentTypeError();
		return new Function.Call(this, args);
	}

}
