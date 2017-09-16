package com.xrbpowered.jpas.system;

import com.xrbpowered.jpas.Scope.EntryType;
import com.xrbpowered.jpas.ast.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Write extends Function {

	private static final Type[] argTypes = {Type.string};
	
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
	public Type[] getArgTypes() {
		return argTypes;
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

}
