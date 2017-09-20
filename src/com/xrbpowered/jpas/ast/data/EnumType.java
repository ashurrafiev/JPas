package com.xrbpowered.jpas.ast.data;

import com.xrbpowered.jpas.ast.exp.Function;

public class EnumType extends Type {

	public class EnumOrdinator extends Type.Ordinator {
		@Override
		public int ord(Object x) {
			return (Integer) x;
		}
		@Override
		public Object unord(int i) {
			return (i & 0x7fffffff) % values;
		}
		@Override
		public Object pred(Object x) {
			return ((Integer) x+values-1) % values;
		}
		@Override
		public Object succ(Object x) {
			return ((Integer) x+1) % values;
		}
	}
	
	public class Converter extends Function {
		@Override
		public Type getType() {
			return EnumType.this;
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
			return getOrdinator().unord((Integer) args[0]);
		}
	}
	
	private int values = 0;
	public final Function converter;
	
	public EnumType() {
		super(false, 0);
		setComparator(Type.integer.getComparator());
		setOrdinator(new EnumOrdinator());
		this.converter = new Converter();
	}
	
	public int addValue() {
		values++;
		return values-1;
	}
	
	public Range getRange() {
		return new Range(this, 0, values-1);
	}
	
}
