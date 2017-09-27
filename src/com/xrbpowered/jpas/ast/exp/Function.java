package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.data.Type;

public abstract class Function implements ScopeEntry {

	public static class Call extends Expression {
		
		protected final Function f;
		protected final Expression[] args;
		
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
				for(int i=0; i<args.length; i++) {
					if(f.isLValue(i))
						v[i] = ((LValue) args[i]).getPointer();
					else
						v[i] = args[i].evaluate();
				}
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

	@Override
	public boolean checkImpl() {
		return true;
	}
	
	public boolean isVarArgs() {
		return false;
	}
	
	public boolean isLValue(int argIndex) {
		return false;
	}
	
	public boolean hasSideEffects() {
		return false;
	}

	public abstract Type getType();
	public abstract int getArgNum();
	public abstract Type getArgType(int argIndex);
	public abstract Object call(Object[] args);
	
	protected void testArgNumber(int numArgs, Expression[] args) {
		int num = args==null ? 0 : args.length;
		if(num<numArgs-1 || !isVarArgs() && num<numArgs)
			throw new JPasError("Too few arguments");
		if(!isVarArgs() && num>numArgs)
			throw new JPasError("Too many arguments");
	}
	
	protected Type getArgType(int i, Expression[] args) {
		return i>=getArgNum() ? getArgType(getArgNum()-1) : getArgType(i);
	}
	
	protected void checkLValue(int index, Expression arg) {
		if(isLValue(index) && !(arg instanceof LValue))
			throw JPasError.lvalueError();
	}
	
	protected Expression checkTypeCast(Type dt, Expression arg) {
		Expression ex = Expression.implicitCast(dt, arg);
		if(ex!=null)
			return ex;
		throw JPasError.argumentTypeError();
	}
	
	public Function.Call makeCall(Expression[] args) {
		testArgNumber(getArgNum(), args);
		if(args!=null) {
			for(int i=0; i<args.length; i++) {
				Type dt = getArgType(i, args);
				args[i] = checkTypeCast(dt, args[i]);
				checkLValue(i, args[i]);
			}
		}
		return new Function.Call(this, args);
	}
	
	public boolean isEquivalent(Function f) {
		// only custom function declarations are checked for equivalence
		return false;
	}

}
