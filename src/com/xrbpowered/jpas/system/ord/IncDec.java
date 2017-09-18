package com.xrbpowered.jpas.system.ord;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.data.Type.Ordinator;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.mem.Pointer;

public class IncDec extends Function {

	private static class IntIncDec extends IncDec {
		protected IntIncDec(Type type, boolean inc) {
			super(type, inc);
		}
		@Override
		public Object call(Object[] args) {
			Pointer ptr = (Pointer) args[0];
			int v = (Integer) ptr.read() + (inc ? 1: -1);
			ptr.write(new Integer(v));
			return null;
		};
	}
	
	private static IncDec intInc = new IntIncDec(Type.integer, true);
	private static IncDec intDec = new IntIncDec(Type.integer, false);

	private final Type type;
	protected final boolean inc;
	
	public IncDec(boolean inc) {
		this(null, inc);
	}

	protected IncDec(Type type, boolean inc) {
		this.type = type;
		this.inc = inc;
	}

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.procedure;
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
	public boolean isLValue(int argIndex) {
		return true;
	}

	@Override
	public Object call(Object[] args) {
		Pointer ptr = (Pointer) args[0];
		Ordinator ord = type.getOrdinator();
		Object v = inc ? ord.succ(ptr.read()) : ord.pred(ptr.read());
		ptr.write(v);
		return null;
	}

	public Function.Call makeCall(Expression[] args) {
		testArgNumber(1, args);
		checkLValue(0, args[0]);
		if(args[0].getType()==Type.integer)
			return new Function.Call(inc ? intInc : intDec, args);
		else if(args[0].getType().getOrdinator()!=null)
			return new Function.Call(this, args);
		else
			throw new JPasError("Argument type mismatch");
	}
	
}
