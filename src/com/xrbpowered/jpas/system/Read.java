package com.xrbpowered.jpas.system;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.Pointer;

public class Read extends Function {

	public static class Call extends Function.Call {

		public Call(Function f, Expression[] args) {
			super(f, args);
		}
		
		@Override
		public Object evaluate() {
			if(args==null)
				((Read) f).call(null, null);
			else {
				Type[] t = new Type[args.length];
				Pointer[] p = new Pointer[args.length];
				for(int i=0; i<args.length; i++) {
					t[i] = args[i].getType();
					p[i] = ((LValue) args[i]).getPointer();
				}
				((Read) f).call(t, p);
			}
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
	
	private final boolean line;
	
	public Read(boolean line) {
		this.line = line;
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
	
	@Override
	public Type getArgType(int argIndex) {
		return null;
	}
	
	public void call(Type[] types, Pointer[] ptrs) {
		Scanner in = getConsole();
		try {
			if(types!=null && ptrs!=null) {
				for(int i=0; i<types.length; i++) {
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
						value = in.nextLine();
					
					ptrs[i].write(value);
				}
			}
			if(line)
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
	
	public Function.Call makeCall(Expression[] args) {
		testArgNumber(getArgNum(), args);
		if(args!=null) {
			for(int i=0; i<args.length; i++) {
				Type type = args[i].getType();
				if(!type.builtIn || type==Type.string && args.length!=1)
					throw new JPasError("Argument type mismatch");
				checkLValue(i, args[i]);
			}
		}
		return new Read.Call(this, args);
	}
}
