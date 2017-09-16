package com.xrbpowered.jpas.system.math;

import java.util.Random;

import com.xrbpowered.jpas.Scope.EntryType;
import com.xrbpowered.jpas.ast.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Randomize extends Function {

	public static final Random random =new Random(0L);

	private static final Type[] argTypes = {Type.integer};

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.procedure;
	}
	
	@Override
	public Type getType() {
		return null;
	}
	
	@Override
	public Type[] getArgTypes() {
		return argTypes;
	}

	@Override
	public Object call(Object[] args) {
		if(args==null)
			random.setSeed(System.currentTimeMillis());
		else
			random.setSeed((Integer) args[0]);
		return null;
	}
	
	@Override
	public Function.Call makeCall(Expression[] args) {
		if(args==null || args.length==0)
			return new Function.Call(this, null);
		else
			return super.makeCall(args);
	}

}
