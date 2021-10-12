package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.mem.Pointer;

public class Delete extends Function {

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
		return argIndex==0 ? Type.string : Type.integer;
	}

	@Override
	public boolean isLValue(int argIndex) {
		return argIndex==0;
	}
	
	private static String delete(String s, int start, int end) {
		int len = s.length();
		if(end<=start)
			return s;
		if(start<0)
			start = 0;
		if(start>=len)
			return s;
		if(end>len)
			end = len;
		if(end<=0)
			return s;
		return s.substring(0, start) + s.substring(end, s.length());
	}
	
	@Override
	public Object call(Object[] args) {
		Pointer strPtr = (Pointer) args[0];
		String s = (String) strPtr.read();
		int start = (Integer) args[1]-1;
		int end = (Integer) args[2]+start;
		strPtr.write(delete(s, start, end));
		return null;
	}

}
