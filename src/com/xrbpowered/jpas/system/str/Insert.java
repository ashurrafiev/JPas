package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.mem.Pointer;

public class Insert extends Function {

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
		return 3;
	}

	@Override
	public Type getArgType(int argIndex) {
		return argIndex<2 ? Type.string : Type.integer;
	}

	@Override
	public boolean isLValue(int argIndex) {
		return argIndex==1;
	}

	@Override
	public Object call(Object[] args) {
		Pointer strPtr = (Pointer) args[1];
		StringBuilder sb = new StringBuilder((String) strPtr.read());
		try {
			sb.insert((Integer) args[2]-1, (String) args[0]);
			strPtr.write(sb.toString());
		}
		catch(StringIndexOutOfBoundsException e) {
		}
		return null;
	}

}
