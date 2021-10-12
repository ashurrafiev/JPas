package com.xrbpowered.jpas.ast.exp;

import java.util.ArrayList;
import java.util.List;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.RecordObject;
import com.xrbpowered.jpas.ast.data.RecordType;
import com.xrbpowered.jpas.ast.data.Type;

public class RecordLiteral extends FluidTypeExpression {

	private final RecordType rt;
	private final List<Expression> vals;
	
	public RecordLiteral(RecordType rt, List<Expression> vals) {
		this.rt = rt;
		this.vals = vals;
	}
	
	@Override
	public Type getType() {
		return rt;
	}
	
	@Override
	public Object evaluate() {
		Object[] v = new Object[vals.size()];
		for(int i=0; i<vals.size(); i++) {
			Expression ex = vals.get(i); 
			v[i] = ex==null ? null : ex.evaluate();
		}
		return new RecordObject(rt.memberTypes(), v);
	}

	@Override
	public boolean isConst() {
		return false;
	}
	
	@Override
	public Expression backPropagateType(Scope scope, Type t) {
		if(!(t instanceof RecordType))
			return null;
		RecordType tt = (RecordType) t;
		int num = tt.memberTypes().size();
		List<Expression> expressions = new ArrayList<>(num);
		for(int i=0; i<num; i++)
			expressions.add(null);
		for(String name : rt.fieldNames()) {
			int index = tt.find(name);
			if(index<0)
				return null;
			Expression ex = Expression.implicitCast(scope, tt.getType(index), vals.get(rt.find(name)));
			if(ex==null)
				return null;
			expressions.set(index, ex);
		}
		RecordLiteral obj = new RecordLiteral(tt, expressions);
		return obj;
	}

	public static Expression make(List<String> names, List<Expression> expressions) {
		RecordType rt = new RecordType(true);
		for(int i=0; i<names.size(); i++)
			rt.add(names.get(i), expressions.get(i).getType());
		RecordLiteral obj = new RecordLiteral(rt, expressions);
		boolean c = true;
		for(Expression ex : expressions) {
			if(!ex.isConst()) {
				c = false;
				break;
			}
		}
		return c ? new Constant(rt, obj.evaluate()) : obj;
	}

}
