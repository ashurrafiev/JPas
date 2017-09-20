package com.xrbpowered.jpas.ast.data;

import java.util.List;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.exp.Equals;
import com.xrbpowered.jpas.mem.RecordItemPointer;

public class RecordObject {

	private Object[] values;
	
	public RecordObject(List<Type> members) {
		this.values = new Object[members.size()];
		for(int i=0; i<values.length; i++)
			values[i] = members.get(i).init(null);
	}
	
	public RecordObject(List<Type> members, RecordObject v) {
		this.values = new Object[members.size()];
		for(int i=0; i<values.length; i++)
			values[i] = members.get(i).init(v.values[i]);
	}
	
	public RecordObject(List<Type> members, Object[] v) {
		this.values = v;
		for(int i=0; i<values.length; i++)
			if(values[i]==null)
				values[i] = members.get(i).init(null);
	}

	public void free() {
		this.values = null;
	}
	
	private void check() {
		if(values==null)
			throw new JPasError("Access violation.");
	}
	
	public Object get(int index) {
		return values[index];
	}
	
	public Object read(int index) {
		check();
		return values[index];
	}

	public void write(int index, Object val) {
		check();
		values[index] = val;
	}

	@Override
	public boolean equals(Object obj) {
		if(this==obj)
			return true;
		if(obj instanceof RecordObject) {
			RecordObject rec = (RecordObject) obj;
			check();
			rec.check();
			for(int i=0; i<values.length; i++)
				if(!Equals.test(values[i], rec.values[i]))
					return false;
			return true;
		}
		return false;
	}
	
	public static void copy(RecordType type, RecordObject dst, RecordObject src) {
		for(int i=0; i<dst.values.length; i++)
			type.getType(i).assign(new RecordItemPointer(dst, i), src.values[i]);
	}

	
}
