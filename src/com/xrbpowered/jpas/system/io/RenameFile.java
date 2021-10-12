package com.xrbpowered.jpas.system.io;

import java.io.File;

import com.xrbpowered.jpas.JPas;
import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.FileObject;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;

public class RenameFile extends FileProc {

	@Override
	public int getArgNum() {
		return 2;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return Type.string;
	}
	
	@Override
	public Object call(Object[] args) {
		FileObject file = (FileObject) ((Pointer) args[0]).read();
		String name = (String) args[1];
		File f = file.getFile();
		checkNotOpen(file);
		checkFile(f);
		try {
			f.renameTo(new File(JPas.workingDir, name));
		}
		catch(Exception e) {
			throw new JPasError("Access denied");
		}
		file.assign(name);
		return null;
	}

}
