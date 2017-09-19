package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Pos extends Function {

	@Override
	public Type getType() {
		return Type.integer;
	}

	@Override
	public int getArgNum() {
		return 2;
	}

	@Override
	public Type getArgType(int argIndex) {
		return Type.string;
	}

	@Override
	public Object call(Object[] args) {
		return new Integer(((String) args[1]).indexOf((String) args[0])+1);
	}

}
