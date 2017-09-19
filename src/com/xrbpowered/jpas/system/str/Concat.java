package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Concat extends Function {

	@Override
	public Type getType() {
		return Type.string;
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
	public Object call(Object[] args) {
		StringBuilder sb = new StringBuilder();
		for(Object a : args)
			sb.append((String) a);
		return sb.toString();
	}
	
}
