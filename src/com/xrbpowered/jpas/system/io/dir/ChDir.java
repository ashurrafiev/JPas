package com.xrbpowered.jpas.system.io.dir;

import java.io.File;

import com.xrbpowered.jpas.JPas;
import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public class ChDir extends Function {

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
	
	protected File getDir(Object[] args) {
		File f = new File(JPas.workingDir, (String) args[0]);
		try {
			f = f.getCanonicalFile();
			if(f.isDirectory())
				return f;
		}
		catch(Exception e) {
		}
		throw new JPasError("Not a directory");
	}

	@Override
	public Object call(Object[] args) {
		JPas.workingDir = getDir(args);
		return null;
	}

}
