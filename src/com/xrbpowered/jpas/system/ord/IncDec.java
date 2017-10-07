package com.xrbpowered.jpas.system.ord;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.data.Type.Ordinator;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.Pointer;

public class IncDec extends Function {

	public static class Call extends Function.Call {

		public Call(Function f, Expression[] args) {
			super(f, args);
		}
		
		@Override
		public Object evaluate() {
			((IncDec) f).call(args[0].getType(), ((LValue) args[0]).getPointer());
			return null;
		}
	}
	
	private static class IntIncDec extends IncDec {
		protected IntIncDec(boolean inc) {
			super(inc);
		}
		
		public void call(Type type, Pointer ptr) {
			int v = (Integer) ptr.read() + (inc ? 1: -1);
			ptr.write(new Integer(v));
		}
	}
	
	private static IncDec intInc = new IntIncDec(true);
	private static IncDec intDec = new IntIncDec(false);

	protected final boolean inc;
	
	public IncDec(boolean inc) {
		this.inc = inc;
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
	public Type getArgType(int argIndex) {
		return null;
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return true;
	}

	public void call(Type type, Pointer ptr) {
		Ordinator ord = type.getOrdinator();
		Object v = inc ? ord.succ(ptr.read()) : ord.pred(ptr.read());
		ptr.write(v);
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
		if(type==Type.integer)
			return new IncDec.Call(inc ? intInc : intDec, args);
		if(type.getOrdinator()!=null)
			return new IncDec.Call(this, args);
		else
			throw JPasError.argumentTypeError();
	}
	
}
