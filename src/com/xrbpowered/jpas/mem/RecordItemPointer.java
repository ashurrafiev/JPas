package com.xrbpowered.jpas.mem;

import com.xrbpowered.jpas.ast.data.RecordObject;

public class RecordItemPointer implements Pointer {

	private final RecordObject rec;
	private final int index;
	
	public RecordItemPointer(RecordObject rec, int index) {
		this.rec = rec;
		this.index = index;
	}
	
	@Override
	public Object read() {
		return rec.read(index);
	}
	
	@Override
	public void write(Object value) {
		rec.write(index, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RecordItemPointer) {
			RecordItemPointer p = (RecordItemPointer) obj;
			return rec==p.rec && index==p.index;
		}
		return false;
	}
	
}
