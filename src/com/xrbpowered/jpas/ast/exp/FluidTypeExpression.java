package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.Type;

public abstract class FluidTypeExpression extends Expression {

	public abstract Expression backPropagateType(Scope scope, Type t);
	
}
