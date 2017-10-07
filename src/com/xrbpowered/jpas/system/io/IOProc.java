package com.xrbpowered.jpas.system.io;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.FileType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;

public abstract class IOProc extends Function {

	public static enum IOType {
		stdio, text, untypedFile, typedFile
	}
	
	@Override
	public EntryType getScopeEntryType() {
		return EntryType.procedure;
	}
	
	@Override
	public Type getType() {
		return null;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return null;
	}
	
	protected IOType getIOType(Scope scope, Expression[] args) {
		IOType io = IOType.stdio;
		if(args==null || args.length<1)
			return io;
		args[0] = FunctionType.dereference(scope, args[0]);
		Type t = args[0].getType();
		if(t==FileType.text)
			io = IOType.text;
		else if(t==FileType.untypedFile)
			io = IOType.untypedFile;
		else if(t instanceof FileType)
			io = IOType.typedFile;
		if(io!=IOType.stdio && !(args[0] instanceof LValue))
			throw JPasError.lvalueError();
		return io;
	}

	protected static JPasError readError() {
		return new JPasError("Cannot read file");
	}

	protected static JPasError writeError() {
		return new JPasError("Cannot write file");
	}

}
