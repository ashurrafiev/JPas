package com.xrbpowered.jpas.system.io;

import com.xrbpowered.jpas.ast.data.FileObject;
import com.xrbpowered.jpas.mem.Pointer;

public class Rewrite extends FileProc {

	private boolean write;
	private boolean append;
	
	public Rewrite(boolean write, boolean append) {
		this.write = write;
	}
	
	@Override
	public Object call(Object[] args) {
		FileObject file = (FileObject) ((Pointer) args[0]).read();
		if(write)
			file.openWrite(append);
		else
			file.openRead();
		return null;
	}
	
}
