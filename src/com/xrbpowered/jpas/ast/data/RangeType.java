package com.xrbpowered.jpas.ast.data;

public class RangeType extends Type {

	public final Range range;
	
	public RangeType(Range range) {
		super(range.type.builtIn, range.type.getOrdinator().unord(range.min));
		this.range = range;
	}
	
	public Type getBaseType() {
		return range.type;
	}

	@Override
	public Comparator getComparator() {
		return range.type.getComparator();
	}
	
	@Override
	public Ordinator getOrdinator() {
		return range.type.getOrdinator();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			if(obj instanceof RangeType)
				return Range.checkEqual(this.range, ((RangeType) obj).range);
			else
				return false;
		}
		else
			return true;
	}
	
}
