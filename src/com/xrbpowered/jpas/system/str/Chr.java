package com.xrbpowered.jpas.system.str;

import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class Chr extends Function {

	@Override
	public Type getType() {
		return Type.character;
	}

	@Override
	public int getArgNum() {
		return 1;
	}

	@Override
	public Type getArgType(int argIndex) {
		return Type.integer;
	}

	@Override
	public Object call(Object[] args) {
		return Type.character.getOrdinator().unord((Integer) args[0]);
	}

}
