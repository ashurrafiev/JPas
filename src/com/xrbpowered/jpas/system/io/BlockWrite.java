package com.xrbpowered.jpas.system.io;

import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.DataFileObject;
import com.xrbpowered.jpas.ast.data.FileType;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;

public class BlockWrite extends IOProc {

	public static class Call extends Function.Call {
		public Call(Function f, Expression[] args) {
			super(f, args);
		}
		@Override
		public Object evaluate() {
			DataFileObject file = (DataFileObject) ((LValue) args[0]).getPointer().read();
			Type type = ((ArrayType) args[1].getType()).type;
			ArrayObject ar = (ArrayObject) args[1].evaluate();
			BlockWrite.call(file, type, ar, (Integer) args[2].evaluate());
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
	
	private static void call(DataFileObject file, Type type, ArrayObject ar, int count) {
		try {
			DataOutputStream out = file.getOutputStream();
			Object[] av = ar.getValues();
			if(count>av.length)
				throw JPasError.rangeCheckError();
			for(int i=0; i<count; i++)
				DataWrite.writeData(out, type, av[i]);
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
		testArgNumber(getArgNum(), args);
		args[2] = checkTypeCast(scope, Type.integer, args[2]);
		args[1] = FunctionType.dereference(scope, args[1]);
		if(!(args[1].getType() instanceof ArrayType))
			throw JPasError.argumentTypeError();
		ArrayType at = (ArrayType) args[1].getType();
		
		IOType io = getIOType(scope, args);
		if(io==IOType.untypedFile) {
			if(!at.type.builtIn)
				throw JPasError.argumentTypeError();
		}
		else if(io==IOType.typedFile) {
			FileType ft = (FileType) args[0].getType();
			if(at.type != ft.type)
				throw JPasError.argumentTypeError();
		}
		else
			throw JPasError.argumentTypeError();		
		return new BlockWrite.Call(this, args);
	}
}
