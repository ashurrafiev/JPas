package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;

public class Fill extends Function {

	public static class Call extends Function.Call {
		private final int depth;
		
		public Call(Function f, Expression[] args, int depth) {
			super(f, args);
			this.depth = depth;
		}
		
		@Override
		public Object evaluate() {
			ArrayObject.fill((ArrayType) args[0].getType(), (ArrayObject) ((LValue) args[0]).getPointer().read(), args[1].evaluate(), depth);
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
		return 2;
	}

	@Override
	public Type getArgType(int argIndex) {
		return null;
	}

	@Override
	public boolean isLValue(int argIndex) {
		return argIndex==0;
	}
	
	@Override
	public Object call(Object[] args) {
		return null;
	}

	private Expression checkArg;
	
	public int checkType(Scope scope, ArrayType at, Type t, int depth) {
		if(at.type.equals(t))
			return depth;
		else {
			Expression ex = Expression.implicitCast(scope, at.type, checkArg);
			if(ex!=null) {
				checkArg = ex;
				return depth;
			}
			else if(at.type instanceof ArrayType)
				return checkType(scope, (ArrayType) at.type, t, depth+1);
			else
				throw JPasError.argumentTypeError();
		}
	}
	
	@Override
	public Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		args[0] = FunctionType.dereference(scope, args[0]);
		checkLValue(0, args[0]);
		Type at = args[0].getType();
		if(at instanceof ArrayType) {
			checkArg= args[1];
			Type t = checkArg.getType();
			int depth = checkType(scope, (ArrayType) at, t, 0);
			args[1] = checkArg;
			return new Fill.Call(this, args, depth);
		}
		else
			throw JPasError.argumentTypeError();
	}
	
}
