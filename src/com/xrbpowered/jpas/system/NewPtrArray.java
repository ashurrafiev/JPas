package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.PointerType;
import com.xrbpowered.jpas.ast.data.Range;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.FreePointer;
import com.xrbpowered.jpas.mem.Pointer;

public class NewPtrArray extends Function {

	public static class Call extends Function.Call {

		public Call(Function f, Expression[] args) {
			super(f, args);
		}
		
		@Override
		public Object evaluate() {
			int[] v = new int[args.length-1];
			for(int i=1; i<args.length; i++) {
				v[i-1] = (Integer) args[i].evaluate();
			}
			NewPtrArray.call((PointerType) args[0].getType(), ((LValue) args[0]).getPointer(), v);
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
	public boolean isVarArgs() {
		return true;
	}
	
	@Override
	public int getArgNum() {
		return 3;
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return argIndex==0;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return argIndex>0 ? Type.integer : null;
	}
	
	public static Type makeType(Type dstType, int[] ranges, int dim) {
		if(dim<ranges.length) {
			ArrayType at = (ArrayType) dstType;
			Type type = makeType(at.type, ranges, dim+1);
			Range r;
			if(at.range!=null) {
				if(at.range.length()!=ranges[dim])
					throw JPasError.rangeCheckError();
				r = at.range;
			}
			else {
				r = new Range(Type.integer, 0, ranges[dim]-1);
			}
			return new ArrayType(r, type);
		}
		else
			return dstType;
	}
	
	public static void call(PointerType type, Pointer ptr, int[] ranges) {
		Type t = makeType(type.getType(), ranges, 0);
		ptr.write(new FreePointer(t));
	}
	
	@Override
	public Object call(Object[] args) {
		return null;
	}
	
	private boolean checkType(Type type, int dims) {
		if(dims>0) {
			if(type!=null && type instanceof ArrayType)
				return checkType(((ArrayType) type).type, dims-1);
			else
				return false;
		}
		else
			return true;
	}
	
	public Function.Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		for(int i=1; i<args.length; i++) {
			Type dt = getArgType(i, args);
			args[i] = checkTypeCast(scope, dt, args[i]);
		}
		args[0] = FunctionType.dereference(scope, args[0]);
		checkLValue(0, args[0]);
		Type type = args[0].getType();
		if(type instanceof PointerType) {
			if(checkType(((PointerType) type).getType(), args.length-1))
				return new NewPtrArray.Call(this, args);
		}
		throw JPasError.argumentTypeError();
	}
	
}
