package com.xrbpowered.jpas.ast.data;

import java.util.List;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Range;
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
	public Type indexType() {
		return range.getType(); // TODO null range
	}
	
	@Override
	public Object init(Object v) {
		ArrayObject ar = new ArrayObject(range.fix()); // TODO optimise range fix for multi-dim arrays
		for(int i=0; i<ar.values.length; i++)
			ar.values[i] = type.init(null);
		return ar;
	}
	
	@Override
	public void assign(Pointer ptr, Object v) {
		throw new JPasError("Array assignment."); // TODO array copy on assign
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj); // TODO array type comparison
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
