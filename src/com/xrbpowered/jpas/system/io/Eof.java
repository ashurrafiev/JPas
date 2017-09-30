package com.xrbpowered.jpas.system.io;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.FileObject;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;

public class Eof extends FileProc {

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.function;
	}
	
	@Override
	public Type getType() {
		return Type.bool;
	}

	@Override
	public Object call(Object[] args) {
		FileObject file = (FileObject) ((Pointer) args[0]).read();
		return file.isEof();
	}

}
