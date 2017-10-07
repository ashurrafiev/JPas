package com.xrbpowered.jpas.ast.data;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.exp.CustomFunction;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.StackFrameDesc;

public class FunctionType extends Type {

	public static class DynamicCall extends Function.Call {
		private final FunctionDeclaration decl;
		private final Expression fex;
		private final StackFrameDesc sf;
		
		public DynamicCall(Scope scope, FunctionDeclaration decl, Expression fex, Expression[] args) {
			super(null, args);
			this.fex = fex;
			this.decl = decl;
			this.sf = scope.stackFrame;
		}
		
		@Override
		public Type getType() {
			return checkResultType(decl.type);
		}
		
		@Override
		public boolean isConst() {
			return false;
		}
		
		@Override
		public Object evaluate() {
			Function f = (Function) fex.evaluate();
			if(f==null)
				throw new JPasError("Undefined function reference");
			if(f!=this.f) {
				this.f = f;
				if(sf==null || !sf.isVisible(((CustomFunction) f).getStackFrame().getParent()))
					throw new JPasError("Access violation");
			}
			Object res = super.evaluate();
			return res;
		}
	}
	
	private final FunctionDeclaration decl;
	
	public FunctionType(FunctionDeclaration decl) {
		super(false, null);
		this.decl = decl;
	}

	@Override
	public boolean isSerialisable() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if(this==obj)
			return true;
		else if(obj!=null && obj instanceof FunctionType) {
			return decl.equals(((FunctionType) obj).decl);
		}
		else
			return false;
	}
	
	protected void testArgNumber(int numArgs, Expression[] args) {
		int num = args==null ? 0 : args.length;
		if(num<numArgs)
			throw JPasError.argumentNumberError(false);
		if(num>numArgs)
			throw JPasError.argumentNumberError(true);
	}
	
	protected Type getArgType(int i, Expression[] args) {
		return decl.argDefs.get(i).type;
	}
	
	protected void checkLValue(int index, Expression arg) {
		if(decl.argDefs.get(index).lvalue && !(arg instanceof LValue))
			throw JPasError.lvalueError();
	}
	
	protected Expression checkTypeCast(Scope scope, Type dt, Expression arg) {
		Expression ex = Expression.implicitCast(scope, dt, arg);
		if(ex!=null)
			return ex;
		throw JPasError.argumentTypeError();
	}
	
	public Function.Call makeCall(Scope scope, Expression fex, Expression[] args) {
		if(fex.isConst()) {
			CustomFunction f = (CustomFunction) fex.evaluate();
			return f.makeCall(scope, args);
		}
		else {
			testArgNumber(decl.numArgs(), args);
			if(args!=null) {
				for(int i=0; i<args.length; i++) {
					Type dt = getArgType(i, args);
					args[i] = checkTypeCast(scope, dt, args[i]);
					checkLValue(i, args[i]);
				}
			}
			return new DynamicCall(scope, decl, fex, args);
		}
	}
	
	public static Expression dereference(Scope scope, Expression ex) {
		Type t = ex.getType();
		if(t instanceof FunctionType) {
			ex = ((FunctionType) t).makeCall(scope, ex, null);
			ex.getType();
		}
		return ex;
	}
}
