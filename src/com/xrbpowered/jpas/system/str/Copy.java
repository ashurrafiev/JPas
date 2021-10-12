package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Copy extends Function {

	@Override
	public Type getType() {
		return Type.string;
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
	public Object call(Object[] args) {
		int start = (Integer) args[1]-1;
		int end = (Integer) args[2]+start;
		String s = (String) args[0];
		int len = s.length();
		if(end<=start)
			return "";
		if(start<0)
			start = 0;
		if(start>=len)
			return "";
		if(end>len)
			end = len;
		if(end<=0)
			return "";
		return s.substring(start, end);
	}

}
