package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.RangeType;
import com.xrbpowered.jpas.ast.data.Type;

public abstract class BinaryOp extends Expression {

	public static enum Operation {
		mul, div, idiv, mod, and, shl, shr,
		add, sub, or, xor,
		eq, neq, gt, lt, ge, le
	}

	private final Type type;
	protected final Expression x, y;

	public BinaryOp(Expression x, Expression y) {
		this(x.getType(), x, y);
	}

	public BinaryOp(Type type, Expression x, Expression y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	public boolean isConst() {
		return x.isConst() && y.isConst();
	}
	
	public static Expression make(Operation op, Expression x, Expression y) {
		Type xt = x.getType();
		Type yt = y.getType();
		Type type = xt;
		
		if(xt instanceof RangeType && yt instanceof RangeType && !xt.equals(yt))
			return null;
		if(yt instanceof RangeType)
			type = yt = ((RangeType) yt).getBaseType();
		if(xt instanceof RangeType)
			type = xt = ((RangeType) xt).getBaseType();
		
		if(xt==Type.string || yt==Type.string) {
			x = ToString.make(x);
			if(x==null)
				return null;
			y = ToString.make(y);
			if(y==null)
				return null;
			type = xt = yt = Type.string;
		}
		
		if(!xt.equals(yt)) {
			if(xt==Type.integer && yt==Type.real || yt==Type.integer && xt==Type.real) {
				x = IntAsReal.make(x);
				y = IntAsReal.make(y);
				type = xt = yt = Type.real;
			}
			else
				return null;
		}
		
		switch(op) {
			case add:
				if(xt==Type.string)
					return new BinaryOp(x, y) {
						@Override
						public Object evaluate() {
							return (String) x.evaluate() + (String) y.evaluate(); // str + str
						}
					};
				else if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int + int
						@Override
						public Object evaluate() {
							return (Integer) x.evaluate() + (Integer) y.evaluate();
						}
					};
				else if(xt==Type.real)
					return new BinaryOp(x, y) { // real + real
						@Override
						public Object evaluate() {
							return (Double) x.evaluate() + (Double) y.evaluate();
						}
					};
				else
					return null;
				
			case sub:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int - int
						@Override
						public Object evaluate() {
							return (Integer) x.evaluate() - (Integer) y.evaluate();
						}
					};
				else if(xt==Type.real)
					return new BinaryOp(x, y) { // real - real
						@Override
						public Object evaluate() {
							return (Double) x.evaluate() - (Double) y.evaluate();
						}
					};
				else
					return null;

			case mul:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int * int
						@Override
						public Object evaluate() {
							return (Integer) x.evaluate() * (Integer) y.evaluate();
						}
					};
				else if(xt==Type.real)
					return new BinaryOp(x, y) { // real * real
						@Override
						public Object evaluate() {
							return (Double) x.evaluate() * (Double) y.evaluate();
						}
					};
				else
					return null;

			case div:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int / int
						@Override
						public Type getType() {
							return Type.real;
						}
						@Override
						public Object evaluate() {
							return (double)((Integer) x.evaluate()) / (double)((Integer) y.evaluate());
						}
					};
				else if(xt==Type.real)
					return new BinaryOp(x, y) { // real / real
						@Override
						public Object evaluate() {
							return (Double) x.evaluate() / (Double) y.evaluate();
						}
					};
				else
					return null;
				
			case idiv:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int div int
						@Override
						public Object evaluate() {
							int yv = (Integer) y.evaluate();
							if(yv==0)
								throw new JPasError("Division by 0.");
							return (Integer) x.evaluate() / yv;
						}
					};
				else
					return null;

			case mod:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int mod int
						@Override
						public Object evaluate() {
							int yv = (Integer) y.evaluate();
							if(yv==0)
								throw new JPasError("Division by 0.");
							return (Integer) x.evaluate() % yv;
						}
					};
				else
					return null;

			case shl:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int shl int
						@Override
						public Object evaluate() {
							return (Integer) x.evaluate() << (Integer) y.evaluate();
						}
					};
				else
					return null;

			case shr:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int shr int
						@Override
						public Object evaluate() {
							return (Integer) x.evaluate() >> (Integer) y.evaluate();
						}
					};
				else
					return null;

			case and:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int and int
						@Override
						public Object evaluate() {
							return (Integer) x.evaluate() & (Integer) y.evaluate();
						}
					};
				else if(xt==Type.bool)
					return new BinaryOp(x, y) { // bool and bool
						@Override
						public Object evaluate() {
							return (Boolean) x.evaluate() && (Boolean) y.evaluate();
						}
					};
				else
					return null;

			case or:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int or int
						@Override
						public Object evaluate() {
							return (Integer) x.evaluate() | (Integer) y.evaluate();
						}
					};
				else if(xt==Type.bool)
					return new BinaryOp(x, y) { // bool or bool
						@Override
						public Object evaluate() {
							return (Boolean) x.evaluate() || (Boolean) y.evaluate();
						}
					};
				else
					return null;

			case xor:
				if(xt==Type.integer)
					return new BinaryOp(type, x, y) { // int xor int
						@Override
						public Object evaluate() {
							return (Integer) x.evaluate() ^ (Integer) y.evaluate();
						}
					};
				else if(xt==Type.bool)
					return new BinaryOp(x, y) { // bool xor bool
						@Override
						public Object evaluate() {
							return (Boolean) x.evaluate() ^ (Boolean) y.evaluate();
						}
					};
				else
					return null;

			case eq:
				return new Equals(type, x, y, false);
				
			case neq:
				return new Equals(type, x, y, true);
				
			case gt:
			case lt:
			case ge:
			case le:
				return CompareOp.make(type, op, x, y);
				
			default:
				return null;
		}
	}

}
