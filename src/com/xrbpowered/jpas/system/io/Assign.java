package com.xrbpowered.jpas.system.io;

import com.xrbpowered.jpas.ast.data.FileObject;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;

public class Assign extends FileProc {

	@Override
	public int getArgNum() {
		return 2;
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return argIndex==0;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return Type.string;
	}
	
	@Override
	public Object call(Object[] args) {
		FileObject file = (FileObject) ((Pointer) args[0]).read();
		checkNotOpen(file);
		file.assign((String) args[1]);
		return null;
	}
	
}
