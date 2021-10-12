package com.xrbpowered.jpas.system.io;

import java.io.DataInputStream;
import java.io.IOException;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.DataFileObject;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.Pointer;

public class ByteRead extends IOProc {

	public static class ArrayCall extends Function.Call {
		public ArrayCall(Function f, Expression[] args) {
			super(f, args);
		}
		@Override
		public Object evaluate() {
			DataFileObject file = (DataFileObject) ((LValue) args[0]).getPointer().read();
			ArrayObject ar = (ArrayObject) ((LValue) args[1]).getPointer().read();
			ByteRead.arrayCall(file, ar, ((LValue) args[2]).getPointer());
			return null;
		}
	}

	public static class StringCall extends Function.Call {
		public StringCall(Function f, Expression[] args) {
			super(f, args);
		}
		@Override
		public Object evaluate() {
			DataFileObject file = (DataFileObject) ((LValue) args[0]).getPointer().read();
			ByteRead.stringCall(file, ((LValue) args[1]).getPointer(), (Integer) args[2].evaluate());
			return null;
		}
	}

	public static class IntsCall extends Function.Call {
		public IntsCall(Function f, Expression[] args) {
			super(f, args);
		}
		@Override
		public Object evaluate() {
			DataFileObject file = (DataFileObject) ((LValue) args[0]).getPointer().read();
			Pointer[] p = new Pointer[args.length-1];
			for(int i=1; i<args.length; i++) {
				p[i-1] = ((LValue) args[i]).getPointer();
			}
			ByteRead.intsCall(file, p);
			return null;
		}
	}

	@Override
	public boolean isLValue(int argIndex) {
		return true;
	}
	
	@Override
	public int getArgNum() {
		return 3;
	}

	private static void arrayCall(DataFileObject file, ArrayObject ar, Pointer ptrMax) {
		try {
			DataInputStream in = file.getInputStream();
			Object[] av = ar.getValues();
			
			int max = (Integer) ptrMax.read();
			int len = max>0 ? max : in.available();
			byte[] bytes = new byte[len];
			len = in.read(bytes, 0, len);
			if(len>av.length)
				throw JPasError.rangeCheckError();
			
			for(int i=0; i<len; i++)
				av[i] = new Integer((int)bytes[i] & 0xff);
			ptrMax.write(len);
		}
		catch(IOException e) {
			throw readError();
		}
	}
	
	private static void stringCall(DataFileObject file, Pointer ptrStr, int max) {
		try {
			DataInputStream in = file.getInputStream();
			int len = max>0 ? max : in.available();
			byte[] bytes = new byte[len];
			len = in.read(bytes, 0, len);
			ptrStr.write(new String(bytes));
		}
		catch(IOException e) {
			throw readError();
		}
	}

	private static void intsCall(DataFileObject file, Pointer[] p) {
		try {
			DataInputStream in = file.getInputStream();
			byte[] bytes = new byte[p.length];
			int len = in.read(bytes, 0, p.length);
			for(int i=0; i<len; i++)
				p[i].write(new Integer((int)bytes[i] & 0xff));
		}
		catch(IOException e) {
			throw readError();
		}
	}

	@Override
	public Object call(Object[] args) {
		return null;
	}

	@Override
	public Call makeCall(Scope scope, Expression[] args) {
		if(args.length<2)
			JPasError.argumentNumberError(false);
		
		IOType io = getIOType(scope, args);
		if(io!=IOType.untypedFile)
			throw JPasError.argumentTypeError();
		
		args[1] = FunctionType.dereference(scope, args[1]);
		checkLValue(1, args[1]);
		Expression dst = args[1];
		if(dst.getType() instanceof ArrayType) {
			if(((ArrayType) dst.getType()).type!=Type.integer)
				throw JPasError.argumentTypeError();
			testArgNumber(getArgNum(), args);
			args[2] = checkTypeCast(scope, Type.integer, args[2]);
			checkLValue(2, args[2]);
			return new ByteRead.ArrayCall(this, args);
		}
		
		dst = Expression.implicitCast(scope, Type.string, args[1]);
		if(dst!=null) {
			testArgNumber(getArgNum(), args);
			args[1] = dst;
			args[2] = checkTypeCast(scope, Type.integer, args[2]);
			return new ByteRead.StringCall(this, args);
		}
		
		dst = Expression.implicitCast(scope, Type.integer, args[1]);
		if(dst!=null) {
			for(int i=1; i<args.length; i++) {
				if(!(args[i] instanceof LValue))
					throw JPasError.lvalueError();
				args[i] = checkTypeCast(scope, Type.integer, args[i]);
			}
			return new ByteRead.IntsCall(this, args);
		}
		throw JPasError.argumentTypeError();
	}
	
}
