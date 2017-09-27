package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Write extends Function {

	private final boolean newLine;
	
	public Write(boolean newLine) {
		this.newLine = newLine;
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
		return 1;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return Type.string;
	}

	@Override
	public Object call(Object[] args) {
		if(args!=null) {
			for(Object a : args)
				System.out.print(a);
		}
		if(newLine)
			System.out.println();
		return null;
	}
	
	public Function.Call makeCall(Expression[] args) {
		testArgNumber(getArgNum(), args);
		if(args!=null) {
			for(int i=0; i<args.length; i++) {
				if(!args[i].getType().builtIn)
					throw JPasError.argumentTypeError();
			}
		}
		return new Function.Call(this, args);
	}

}
