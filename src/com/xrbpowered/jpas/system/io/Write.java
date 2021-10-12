package com.xrbpowered.jpas.system.io;

import java.io.PrintStream;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.FileType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.TextFileObject;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;

public class Write extends IOProc {

	public static class Call extends Function.Call {
		private final Expression fileArg;
		public Call(Function f, Expression fileArg, Expression[] args) {
			super(f, args);
			this.fileArg = fileArg;
		}
		@Override
		public Object evaluate() {
			int startArg = fileArg==null ? 0 : 1;
			PrintStream in = fileArg==null ? System.out : ((TextFileObject) fileArg.evaluate()).getPrintWriter();
			Object[] v = null;
			if(args!=null) {
				v = new Object[args.length];
				for(int i=startArg; i<args.length; i++) {
					v[i] = args[i].evaluate();
				}
			}
			((Write) f).call(in, startArg, v);
			return null;
		}
	}
	
	private final boolean newLine;
	
	public Write(boolean newLine) {
		this.newLine = newLine;
	}
	
	@Override
	public boolean isVarArgs() {
		return true;
	}
	
	@Override
	public int getArgNum() {
		return newLine ? 1 : 2;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return Type.string;
	}

	private void call(PrintStream out, int startArg, Object[] v) {
		if(v!=null) {
			for(int i=startArg; i<v.length; i++)
				out.print(v[i]);
		}
		if(newLine)
			out.println();
	}
	
	@Override
	public Object call(Object[] args) {
		return null;
	}
	
	public Function.Call makeCall(Scope scope, Expression[] args) {
		IOType io = getIOType(scope, args);
		int startArg = io!=IOType.stdio ? 1 : 0;
		Expression fileArg = io!=IOType.stdio ? args[0] : null;
		testArgNumber(getArgNum()+startArg, args);
		
		if(io==IOType.stdio || io==IOType.text) {
			if(args!=null) {
				for(int i=startArg; i<args.length; i++) {
					args[i] = FunctionType.dereference(scope, args[i]);
					if(!args[i].getType().builtIn)
						throw JPasError.argumentTypeError();
				}
			}
			return new Write.Call(this, fileArg, args);
		}
		else {
			if(newLine)
				throw JPasError.argumentTypeError();
			
			if(io==IOType.untypedFile) {
				for(int i=startArg; i<args.length; i++) {
					args[i] = FunctionType.dereference(scope, args[i]);
					if(!args[i].getType().builtIn)
						throw JPasError.argumentTypeError();
				}
				return new DataWrite.Call(DataWrite.write, args);
			}
			else if(io==IOType.typedFile) {
				FileType ft = (FileType) args[0].getType();
				for(int i=1; i<args.length; i++) {
					args[i] = FunctionType.dereference(scope, args[i]);
					if(args[i].getType() != ft.type)
						throw JPasError.argumentTypeError();
				}
				return new DataWrite.Call(DataWrite.write, args);
			}
			else
				throw JPasError.argumentTypeError();
		}
	}

}
