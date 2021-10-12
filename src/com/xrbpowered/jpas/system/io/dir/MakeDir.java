package com.xrbpowered.jpas.system.io.dir;

import java.io.File;

import com.xrbpowered.jpas.JPas;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.system.io.FileProc;

public class MakeDir extends Function {

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.procedure;
	}
	
	@Override
	public Type getType() {
		return null;
	}

	@Override
	public int getArgNum() {
		return 1;
	}

	@Override
	public Type getArgType(int argIndex) {
		return Type.string;
	}

	@Override
	public Object call(Object[] args) {
		File f = new File(JPas.workingDir, (String) args[0]);
		try {
			if(f.mkdir())
				return null;
		}
		catch(Exception e) {
		}
		throw FileProc.accessDenied();
	}

}
