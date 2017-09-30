package com.xrbpowered.jpas.system.io;

import com.xrbpowered.jpas.ast.data.FileObject;
import com.xrbpowered.jpas.mem.Pointer;

public class Close extends FileProc {

	private final boolean close;
	public Close(boolean close) {
		this.close = close;
	}
	
	@Override
	public Object call(Object[] args) {
		FileObject file = (FileObject) ((Pointer) args[0]).read();
		if(close)
			file.closeIfOpen();
		else
			file.flush();
		return null;
	}
	
}
