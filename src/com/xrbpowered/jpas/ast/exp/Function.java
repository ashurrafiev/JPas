package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.data.Type;

public abstract class Function implements ScopeEntry {

	public static class Call extends Expression {
		
		private final Function f;
		private final Expression[] args;
		
		public Call(Function f, Expression[] args) {
			this.f = f;
			this.args = args;
		}
		
		@Override
		public Type getType() {
			return f.getType();
		}
		
		@Override
		public Object evaluate() {
			if(args==null)
				return f.call(null);
			else {
				Object[] v = new Object[args.length];
				for(int i=0; i<args.length; i++)
					v[i] = args[i].evaluate();
				return f.call(v);
			}
		}
		
		@Override
		public boolean isConst() {
			if(f.hasSideEffects() || f.getScopeEntryType()==EntryType.procedure || args==null)
				return false;
			else {
				for(Expression a : args)
					if(!a.isConst())
						return false;
			}
			return true;
		}
	}
	
	@Override
	public EntryType getScopeEntryType() {
		return EntryType.function;
	}
	
	public boolean isVarArgs() {
		return false;
	}
	
	public boolean isLValue(int argIndex) {
		return false; // TODO is arg lvalue
	}
	
	public boolean hasSideEffects() {
		return false;
	}

	public abstract Type getType();
	public abstract Type[] getArgTypes();
	public abstract Object call(Object[] args);
	
	protected void testArgNumber(int numArgs, Expression[] args) {
		int num = args==null ? 0 : args.length;
		if(num<numArgs-1 || !isVarArgs() && num<numArgs)
			throw new JPasError("Too few arguments");
		if(!isVarArgs() && num>numArgs)
			throw new JPasError("Too many arguments");
	}
	
	protected Type getArgType(int i, Expression[] args) {
		Type[] argTypes = getArgTypes();
		return i>=argTypes.length ? argTypes[argTypes.length-1] : argTypes[i];
	}
	
	protected Expression checkTypeCast(Type dt, Expression arg) {
		Expression ex = Expression.implicitCast(dt, arg);
		if(ex!=null)
			return ex;
		throw new JPasError("Argument type mismatch");
	}
	
	public Function.Call makeCall(Expression[] args) {
		testArgNumber(getArgTypes().length, args);
		if(args!=null) {
			for(int i=0; i<args.length; i++) {
				Type dt = getArgType(i, args);
				args[i] = checkTypeCast(dt, args[i]);
			}
		}
		return new Function.Call(this, args);
	}

}
