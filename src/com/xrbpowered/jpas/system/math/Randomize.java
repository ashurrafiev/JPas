package com.xrbpowered.jpas.system.math;

import java.util.Random;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Randomize extends Function {

	public static final Random random =new Random(0L);

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
		return 1;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return Type.integer;
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
	public Function.Call makeCall(Scope scope, Expression[] args) {
		if(args==null || args.length==0)
			return new Function.Call(this, null);
		else
			return super.makeCall(scope, args);
	}

}
