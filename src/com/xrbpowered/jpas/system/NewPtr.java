package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.PointerType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.FreePointer;
import com.xrbpowered.jpas.mem.Pointer;

public class NewPtr extends Function {

	public static class Call extends Function.Call {

		public Call(Function f, Expression[] args) {
			super(f, args);
		}
		
		@Override
		public Object evaluate() {
			((NewPtr) f).call((PointerType) args[0].getType(), ((LValue) args[0]).getPointer());
			return null;
		}
	}
	
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
		return 1;
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return true;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return null;
	}
	
	public void call(PointerType type, Pointer ptr) {
		ptr.write(new FreePointer(type.getType()));
	}
	
	@Override
	public Object call(Object[] args) {
		return null;
	}
	
	public Function.Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		args[0] = FunctionType.dereference(scope, args[0]);
		checkLValue(0, args[0]);
		Type type = args[0].getType();
		if(type instanceof PointerType)
			return new NewPtr.Call(this, args);
		else
			throw JPasError.argumentTypeError();
	}
	
}
