package com.xrbpowered.jpas.ast.exp;

import java.util.ArrayList;
import java.util.List;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.Range;
import com.xrbpowered.jpas.ast.data.Type;

public class ArrayLiteral extends FluidTypeExpression {

	private ArrayType at;
	private final List<Expression> vals;
	
	public ArrayLiteral(ArrayType at, List<Expression> vals) {
		this.at = at;
		this.vals = vals;
	}
	
	public void rewriteType(ArrayType at) {
		this.at = at;
	}
	
	@Override
	public Type getType() {
		return at;
	}

	@Override
	public Object evaluate() {
		Object[] v = new Object[vals.size()];
		for(int i=0; i<vals.size(); i++)
			v[i] = vals.get(i).evaluate();
		return new ArrayObject(at.range, v);
	}

	@Override
	public boolean isConst() {
		return false;
	}

	@Override
	public Expression backPropagateType(Scope scope, Type t) {
		if(!(t instanceof ArrayType))
			return null;
		ArrayType tt = (ArrayType) t;
		List<Expression> expressions = new ArrayList<>();
		for(int i=0; i<at.range.length(); i++) {
			Expression ex = Expression.implicitCast(scope, tt.type, vals.get(i));
			if(ex==null)
				return null;
			expressions.add(ex);
		}
		at = new ArrayType(new Range(tt.range==null ? Type.integer : tt.range.type, at.range.min, at.range.max), tt.type);
		ArrayLiteral obj = new ArrayLiteral(at, expressions);
		return obj;
	}
	
	public static Expression make(Type type, List<Expression> expressions) {
		ArrayType at = new ArrayType(new Range(null, 0, expressions.size()-1), type);
		ArrayLiteral obj = new ArrayLiteral(at, expressions);
		boolean c = true;
		for(Expression ex : expressions) {
			if(!type.equals(ex.getType()))
				throw new JPasError("Array literal error.");
			if(!ex.isConst()) {
				c = false;
				break;
			}
		}
		return c ? new Constant(at, obj.evaluate()) : obj;
	}
	
}
