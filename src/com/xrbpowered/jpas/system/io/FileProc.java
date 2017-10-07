package com.xrbpowered.jpas.system.io;

import java.io.File;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.FileObject;
import com.xrbpowered.jpas.ast.data.FileType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public abstract class FileProc extends Function {

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
	public boolean isLValue(int argIndex) {
		return argIndex==0;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return null;
	}

	public static void checkNotOpen(FileObject file) {
		if(file.isOpen())
			throw new JPasError("File is open");
	}

	public static void checkFile(File f) {
		if(!f.isFile())
			throw new JPasError("Not a file");
	}

	public static JPasError accessDenied() {
		return new JPasError("Access denied");
	}

	public Function.Call makeCall(Scope scope, Expression[] args) {
		testArgNumber(getArgNum(), args);
		if(args.length>0) {
			for(int i=1; i<args.length; i++) {
				Type dt = getArgType(i, args);
				args[i] = checkTypeCast(scope, dt, args[i]);
				checkLValue(i, args[i]);
			}
		}
		args[0] = FunctionType.dereference(scope, args[0]);
		checkLValue(0, args[0]);
		Type type = args[0].getType();
		if(type instanceof FileType) {
			return new Function.Call(this, args);
		}
		throw JPasError.argumentTypeError();
	}

}
