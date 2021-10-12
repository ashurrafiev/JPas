package com.xrbpowered.jpas.ast.data;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.mem.Pointer;

public class Type implements ScopeEntry {

	public abstract static class Comparator {
		public abstract int compare(Object x, Object y);
	}
	
	public abstract static class Ordinator {
		public abstract int ord(Object x);
		public abstract Object unord(int i);		
		public abstract Object pred(Object x);
		public abstract Object succ(Object x);
	}
	
	public static final Type integer = new Type(true, 0).setComparator(new Comparator() {
		@Override
		public int compare(Object x, Object y) {
			return Integer.compare((Integer) x, (Integer) y);
		}
	}).setOrdinator(new Ordinator() {
		@Override
		public int ord(Object x) {
			return (Integer) x;
		}
		@Override
		public Object unord(int i) {
			return new Integer(i);
		}
		@Override
		public Object pred(Object x) {
			return (Integer) x-1;
		}
		@Override
		public Object succ(Object x) {
			return (Integer) x+1;
		}
	});
	
	public static final Type real = new Type(true, 0.0).setComparator(new Comparator() {
		@Override
		public int compare(Object x, Object y) {
			return Double.compare((Double) x, (Double) y);
		}
	});
	
	public static final Type bool = new Type(true, false).setComparator(new Comparator() {
		@Override
		public int compare(Object x, Object y) {
			return Boolean.compare((Boolean) x, (Boolean) y);
		}
	}).setOrdinator(new Ordinator() {
		@Override
		public int ord(Object x) {
			return (Boolean) x ? 1 : 0;
		}
		@Override
		public Object unord(int i) {
			return new Boolean((i&1)!=0);
		}
		@Override
		public Object pred(Object x) {
			return !(Boolean) x;
		}
		@Override
		public Object succ(Object x) {
			return !(Boolean) x;
		}
	});
	
	public static final Type character = new Type(true, '\0').setComparator(new Comparator() {
		@Override
		public int compare(Object x, Object y) {
			return Character.compare((Character) x, (Character) y);
		}
	}).setOrdinator(new Ordinator() {
		@Override
		public int ord(Object x) {
			return (Character) x;
		}
		@Override
		public Object unord(int i) {
			return new Character((char) i);
		}
		@Override
		public Object pred(Object x) {
			return unord(ord(x)-1);
		}
		@Override
		public Object succ(Object x) {
			return unord(ord(x)+1);
		}

	});
	
	public static final Type string = new IndexableType(true, "") {
		@Override
		public Type indexType() {
			return Type.integer;
		};
	}.setComparator(new Comparator() {
		@Override
		public int compare(Object x, Object y) {
			return ((String) x).compareTo((String) y);
		}
	});
	
	public final boolean builtIn;
	private Comparator comparator = null;
	private Ordinator ordinator = null;
	
	private Object defValue = null;
	
	protected Type(boolean builtIn, Object defValue) {
		this.builtIn = builtIn;
		this.defValue = defValue;
	}
	
	protected Type setComparator(Comparator comp) {
		this.comparator = comp;
		return this;
	}
	
	public Comparator getComparator() {
		return comparator;
	}

	protected Type setOrdinator(Ordinator ord) {
		this.ordinator = ord;
		return this;
	}
	
	public Ordinator getOrdinator() {
		return ordinator;
	}
	
	public boolean isFluid() {
		return false;
	}

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.type;
	}
	
	@Override
	public boolean checkImpl() {
		return true;
	}
	
	public boolean isSerialisable() {
		return true;
	}
	
	public boolean isInitialisable() {
		return true;
	}
	
	public Object init(Object v) {
		return v==null ? defValue : v;
	}
	
	public void free(Pointer ptr) {
	}
	
	public void assign(Pointer ptr, Object v) {
		ptr.write(v);
	}

	@Override
	public boolean equals(Object obj) {
		return this==obj;
	}
	
	public static boolean checkEqual(Type tx, Type ty) {
		return tx==ty || tx!=null && ty!=null && tx.equals(ty);
	}
	
}
