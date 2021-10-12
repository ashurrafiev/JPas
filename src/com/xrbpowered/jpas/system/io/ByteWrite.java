package com.xrbpowered.jpas.system.io;

import java.io.DataOutputStream;
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

public class ByteWrite extends IOProc {

	public static class ArrayCall extends Function.Call {
		public ArrayCall(Function f, Expression[] args) {
			super(f, args);
		}
		@Override
		public Object evaluate() {
			DataFileObject file = (DataFileObject) ((LValue) args[0]).getPointer().read();
			ArrayObject ar = (ArrayObject) args[1].evaluate();
			ByteWrite.arrayCall(file, ar, (Integer) args[2].evaluate());
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
			ByteWrite.stringCall(file, (String) args[1].evaluate(), (Integer) args[2].evaluate());
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
			Object[] v = new Pointer[args.length-1];
			for(int i=1; i<args.length; i++) {
				v[i-1] = args[i].evaluate();
			}
			ByteWrite.intsCall(file, v);
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

	private static void arrayCall(DataFileObject file, ArrayObject ar, int count) {
		try {
			DataOutputStream out = file.getOutputStream();
			Object[] av = ar.getValues();
			if(count>av.length)
				throw JPasError.rangeCheckError();
			
			int len = count>0 ? count : av.length;
			byte[] bytes = new byte[len];
			for(int i=0; i<len; i++)
				bytes[i] = (byte)(int)((Integer) av[i]);

			out.write(bytes);
		}
		catch(IOException e) {
			throw writeError();
		}
	}
	
	private static void stringCall(DataFileObject file, String str, int count) {
		try {
			DataOutputStream out = file.getOutputStream();
			byte[] bytes = str.getBytes();
			out.write(bytes, 0, count>0 ? count : bytes.length);
		}
		catch(IOException e) {
			throw writeError();
		}
	}

	private static void intsCall(DataFileObject file, Object[] v) {
		try {
			DataOutputStream out = file.getOutputStream();
			byte[] bytes = new byte[v.length];
			for(int i=0; i<v.length; i++)
				bytes[i] = (byte)(int)((Integer) v[i]);
			out.write(bytes);
		}
		catch(IOException e) {
			throw writeError();
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
		Expression dst = args[1];
		if(dst.getType() instanceof ArrayType) {
			if(((ArrayType) dst.getType()).type!=Type.integer)
				throw JPasError.argumentTypeError();
			testArgNumber(getArgNum(), args);
			args[2] = checkTypeCast(scope, Type.integer, args[2]);
			return new ByteWrite.ArrayCall(this, args);
		}
		
		dst = Expression.implicitCast(scope, Type.string, args[1]);
		if(dst!=null) {
			testArgNumber(getArgNum(), args);
			args[1] = dst;
			args[2] = checkTypeCast(scope, Type.integer, args[2]);
			return new ByteWrite.StringCall(this, args);
		}
		
		dst = Expression.implicitCast(scope, Type.integer, args[1]);
		if(dst!=null) {
			for(int i=1; i<args.length; i++) {
				args[i] = checkTypeCast(scope, Type.integer, args[i]);
			}
			return new ByteWrite.IntsCall(this, args);
		}
		throw JPasError.argumentTypeError();
	}

}
