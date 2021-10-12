package com.xrbpowered.jpas.system.io;

import java.io.File;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.FileObject;
import com.xrbpowered.jpas.mem.Pointer;

public class EraseFile extends FileProc {

	@Override
	public Object call(Object[] args) {
		FileObject file = (FileObject) ((Pointer) args[0]).read();
		File f = file.getFile();
		checkNotOpen(file);
		checkFile(f);
		try {
			f.delete();
		}
		catch(Exception e) {
			throw new JPasError("Access denied");
		}
		return null;
	}

}
