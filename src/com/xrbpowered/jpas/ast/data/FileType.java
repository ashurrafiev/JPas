package com.xrbpowered.jpas.ast.data;

import com.xrbpowered.jpas.mem.Pointer;

public class FileType extends Type {

	public static final FileType text= new FileType.Text();
	public static final FileType untypedFile= new FileType(null);
	
	public static class Text extends FileType {
		protected Text() {
			super(null);
		}
		
		@Override
		public Object init(Object v) {
			return v==null ? new TextFileObject() : v;
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj==text;
		}
	}
	
	public final Type type;
	
	protected FileType(Type type) {
		super(false, null);
		this.type = type;
	}

	@Override
	public boolean isSerialisable() {
		return false;
	}
	
	@Override
	public Object init(Object v) {
		return v==null ? new DataFileObject() : v;
	}
	
	@Override
	public void free(Pointer ptr) {
		((FileObject) ptr.read()).closeIfOpen();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof FileType) {
			FileType ft = (FileType) obj;
			return (type!=null && ft.type!=null && type.equals(ft.type)) || (type==null && ft.type==null);
		}
		return false;
	}
	
	public static FileType make(Type type) {
		return type==null ? untypedFile : new FileType(type);
	}
	
}
