package com.xrbpowered.jpas.system.io.dir;

import com.xrbpowered.jpas.JPas;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class GetDir extends Function {

	@Override
	public Type getType() {
		return Type.string;
	}

	@Override
	public int getArgNum() {
		return 0;
	}

	@Override
	public Type getArgType(int argIndex) {
		return null;
	}

	@Override
	public Object call(Object[] args) {
		return JPas.workingDir.getAbsolutePath();
	}

}
