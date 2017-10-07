package com.xrbpowered.jpas.system.io;

import java.util.Scanner;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.TextFileObject;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.Pointer;

public class ReadLn extends IOProc {

	public static class Call extends Function.Call {
		private final Expression fileArg;
		public Call(Function f, Expression fileArg, Expression[] args) {
			super(f, args);
			this.fileArg = fileArg;
		}
		@Override
		public Object evaluate() {
			int startArg = fileArg==null ? 0 : 1;
			Scanner in = fileArg==null ? Read.getConsole() : ((TextFileObject) fileArg.evaluate()).getScanner();
			if(args==null)
				ReadLn.call(in, null);
			else
				ReadLn.call(in, ((LValue) args[startArg]).getPointer());
			return null;
		}
	}
	
	@Override
	public int getArgNum() {
		return 1;
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return true;
	}
	
	private static void call(Scanner in, Pointer ptrStr) {
		String line = in.nextLine();
		if(ptrStr!=null)
			ptrStr.write(line);
	}
	
	@Override
	public Object call(Object[] args) {
		return null;
	}
	
	@Override
	public Function.Call makeCall(Scope scope, Expression[] args) {
		IOType io = getIOType(scope, args);
		int startArg = io!=IOType.stdio ? 1 : 0;
		Expression fileArg = io!=IOType.stdio ? args[0] : null;
		
		if(io==IOType.stdio || io==IOType.text) {
			if(args==null || args.length==startArg)
				return new ReadLn.Call(this, fileArg, null);
			else {
				args[startArg] = FunctionType.dereference(scope, args[startArg]);
				if(args.length==startArg+1 && args[startArg].getType()==Type.string) {
					checkLValue(startArg, args[startArg]);
					return new ReadLn.Call(this, fileArg, args);
				}
				else
					return Read.readLn.makeCall(scope, args);
			}
		}
		else
			throw JPasError.argumentTypeError();
	}

}
