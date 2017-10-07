package com.xrbpowered.jpas.system.io;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.FileType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.TextFileObject;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.Pointer;

public class Read extends IOProc {

	public static class Call extends Function.Call {
		private final Expression fileArg;
		public Call(Function f, Expression fileArg, Expression[] args) {
			super(f, args);
			this.fileArg = fileArg;
		}
		@Override
		public Object evaluate() {
			int startArg = fileArg==null ? 0 : 1;
			Scanner in = fileArg==null ? getConsole() : ((TextFileObject) fileArg.evaluate()).getScanner();
			Type[] t = new Type[args.length];
			Pointer[] p = new Pointer[args.length];
			for(int i=startArg; i<args.length; i++) {
				t[i] = args[i].getType();
				p[i] = ((LValue) args[i]).getPointer();
			}
			((Read) f).call(in, startArg, t, p);
			return null;
		}
	}
	
	public static final Read readLn = new Read(true);
	private static Scanner console = null;
	
	public static Scanner getConsole() {
		if(console==null)
			console = new Scanner(System.in);
		return console;
	}
	
	private final boolean newLine;
	
	public Read(boolean line) {
		this.newLine = line;
	}
	
	@Override
	public boolean isVarArgs() {
		return true;
	}
	
	@Override
	public int getArgNum() {
		return 2;
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return true;
	}
	
	private void call(Scanner in, int startArg, Type[] types, Pointer[] ptrs) {
		try {
			for(int i=startArg; i<types.length; i++) {
				Object value = null;
				Type t = types[i];
				
				if(!in.hasNext())
					throw new JPasError("No input.");
				if(t==Type.integer)
					value = in.nextInt();
				else if(t==Type.real)
					value = in.nextDouble();
				else if(t==Type.bool)
					value = in.nextBoolean();
				else if(t==Type.character) {
					String c = in.next();
					if(c.length()!=1)
						throw new InputMismatchException();
					value = c.charAt(0);
				}
				else if(t==Type.string)
					value = in.next();
				
				ptrs[i].write(value);
			}
			if(newLine)
				in.nextLine();
			return;
		}
		catch(InputMismatchException e) {
			throw new JPasError("Input format error.");
		}
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
		testArgNumber(getArgNum()+startArg, args);

		if(io==IOType.stdio || io==IOType.text) {
			for(int i=startArg; i<args.length; i++) {
				args[i] = FunctionType.dereference(scope, args[i]);
				Type type = args[i].getType();
				if(!type.builtIn)
					throw JPasError.argumentTypeError();
				checkLValue(i, args[i]);
			}
			return new Read.Call(this, fileArg, args);
		}
		else if(io==IOType.untypedFile) {
			for(int i=1; i<args.length; i++) {
				args[i] = FunctionType.dereference(scope, args[i]);
				Type type = args[i].getType();
				if(!type.builtIn)
					throw JPasError.argumentTypeError();
				checkLValue(i, args[i]);
			}
			return new DataRead.Call(DataRead.read, args);
		}
		else if(io==IOType.typedFile) {
			FileType ft = (FileType) args[0].getType();
			for(int i=1; i<args.length; i++) {
				args[i] = FunctionType.dereference(scope, args[i]);
				Type type = args[i].getType();
				if(type != ft.type)
					throw JPasError.argumentTypeError();
				checkLValue(i, args[i]);
			}
			return new DataRead.Call(DataRead.read, args);
		}
		else
			throw JPasError.argumentTypeError();
	}
}
