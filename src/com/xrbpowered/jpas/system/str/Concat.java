package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.mem.Pointer;

public class Concat extends Function {

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
	public Type getArgType(int argIndex) {
		return Type.string;
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return argIndex==0;
	}

	@Override
	public Object call(Object[] args) {
		Pointer strPtr = (Pointer) args[0];
		StringBuilder sb = new StringBuilder((String) strPtr.read());
		for(int i=1; i<args.length; i++)
			sb.append((String) args[i]);
		strPtr.write(sb.toString());
		return null;
	}
	
}
