package com.xrbpowered.jpas.system.io;

import java.io.File;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.FileObject;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;

public class FileSize extends FileProc {

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.function;
	}
	
	@Override
	public Type getType() {
		return Type.integer;
	}
	
	@Override
	public Object call(Object[] args) {
		FileObject file = (FileObject) ((Pointer) args[0]).read();
		File f = file.getFile();
		checkNotOpen(file);
		checkFile(f);
		try {
			return new Integer((int) f.length());
		}
		catch(Exception e) {
			throw new JPasError("Access denied");
		}
	}

}
