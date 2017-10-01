package com.xrbpowered.jpas.ast.data;

import java.util.List;

import com.xrbpowered.jpas.mem.Pointer;

public class ArrayType extends IndexableType {

	public final Range range;
	public final Type type;
	
	public ArrayType(Range range, Type t) {
		super(false, null);
		this.range = range;
		this.type = t;
	}
	
	@Override
	public boolean isFluid() {
		return range!=null && range.type==null;
	}
	
	@Override
	public Type indexType() {
		return range==null ? Type.integer : range.type;
	}
	
	@Override
	public boolean isSerialisable() {
		return range!=null && type.isSerialisable();
	}
	
	@Override
	public boolean isInitialisable() {
		return range!=null;
	}
	
	@Override
	public Object init(Object v) {
		Range r = range;
		if(r==null && v!=null)
			r = ((ArrayObject) v).range;
		if(v==null)
			return new ArrayObject(r, type);
		else
			return new ArrayObject(r, type, (ArrayObject) v);
	}
	
	@Override
	public void free(Pointer ptr) {
		((ArrayObject) ptr.read()).free();
	}
	
	@Override
	public void assign(Pointer ptr, Object v) {
		ArrayObject.copy(this, (ArrayObject) ptr.read(), (ArrayObject) v);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			if(obj instanceof ArrayType) {
				ArrayType at = (ArrayType) obj;
				return type.equals(at.type) && Range.checkEqual(range, at.range);
			}
			return false;
		}
		else
			return true;
	}
	
	public static Type make(List<Range> r, Type type) {
		if(r==null)
			return new ArrayType(null, type);
		else {
			Type t = type;
			for(int i=r.size()-1; i>=0; i--)
				t = new ArrayType(r.get(i), t);
			return t;
		}
	}
	
}
