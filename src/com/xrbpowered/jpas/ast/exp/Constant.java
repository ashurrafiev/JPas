package com.xrbpowered.jpas.ast.exp;

import java.util.ArrayList;
import java.util.List;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.PointerType;
import com.xrbpowered.jpas.ast.data.Range;
import com.xrbpowered.jpas.ast.data.RecordObject;
import com.xrbpowered.jpas.ast.data.RecordType;
import com.xrbpowered.jpas.ast.data.Type;

public class Constant extends FluidTypeExpression implements ScopeEntry {

	private final Object val;
	private final Type type;
	
	public Constant(Type type, Object val) {
		this.type = type;
		this.val = val;
	}
	
	@Override
	public EntryType getScopeEntryType() {
		return EntryType.variable;
	}
	
	@Override
	public boolean checkImpl() {
		return true;
	}

	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	public Object evaluate() {
		return val;
	}
	
	@Override
	public boolean isConst() {
		return true;
	}
	
	@Override
	public Expression backPropagateType(Scope scope, Type t) {
		if(type instanceof ArrayType) {
			ArrayType at = (ArrayType) type;
			if(!(t instanceof ArrayType))
				return null;
			ArrayType tt = (ArrayType) t;
			ArrayObject ar = (ArrayObject) val;
			List<Expression> expressions = new ArrayList<>();
			for(int i=0; i<at.range.length(); i++) {
				Expression ex = new Constant(at.type, ar.get(i));
				ex = Expression.implicitCast(scope, tt.type, ex);
				if(ex==null || !ex.isConst())
					return null;
				expressions.add(ex);
			}
			at = new ArrayType(new Range(tt.range==null ? Type.integer : tt.range.type, at.range.min, at.range.max), tt.type);
			ArrayLiteral obj = new ArrayLiteral(at, expressions);
			return new Constant(at, obj.evaluate());
		}
		else if(type instanceof RecordType) {
			RecordType rt = (RecordType) type;
			if(!(t instanceof RecordType))
				return null;
			RecordType tt = (RecordType) t;
			RecordObject rec = (RecordObject) val;
			int num = tt.memberTypes().size();
			List<Expression> expressions = new ArrayList<>(num);
			for(int i=0; i<num; i++)
				expressions.add(null);
			for(String name : rt.fieldNames()) {
				int index = tt.find(name);
				if(index<0)
					return null;
				int i = rt.find(name);
				Expression ex = new Constant(rt.getType(i), rec.get(i));
				ex = Expression.implicitCast(scope, tt.getType(index), ex);
				if(ex==null || !ex.isConst())
					return null;
				expressions.set(index, ex);
			}
			RecordLiteral obj = new RecordLiteral(tt, expressions);
			return new Constant(tt, obj.evaluate());
		}
		else
			return null;
	}
	
	public static Constant parseNumber(String s) {
		try {
			int v;
			boolean ch = false;
			if(s.charAt(0)=='#') {
				ch = true;
				s = s.substring(1);
			}
			if(s.charAt(0)=='$') {
				v = (int) Long.parseLong(s.substring(1), 16);
			}
			else
				v = Integer.parseInt(s);
			if(ch)
				return new Constant(Type.character, new Character((char) v));
			else
				return new Constant(Type.integer, v);
		}
		catch(NumberFormatException ei) {
			try {
				return new Constant(Type.real, Double.parseDouble(s));
			}
			catch(NumberFormatException ed) {
				throw new JPasError("Number format error.");
			}
		}
	}
	
	public static final Constant constTrue = new Constant(Type.bool, true);
	public static final Constant constFalse = new Constant(Type.bool, false);
	public static final Constant constNil = new Constant(new PointerType(null), null); // TODO null type
	public static final Constant constPi = new Constant(Type.real, Math.PI);
}
