package com.xrbpowered.jpas.system.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.DataFileObject;
import com.xrbpowered.jpas.ast.data.EnumType;
import com.xrbpowered.jpas.ast.data.RangeType;
import com.xrbpowered.jpas.ast.data.RecordObject;
import com.xrbpowered.jpas.ast.data.RecordType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.Pointer;

public class DataRead extends IOProc {

	public static class Call extends Function.Call {
		public Call(Function f, Expression[] args) {
			super(f, args);
		}
		@Override
		public Object evaluate() {
			DataFileObject file = (DataFileObject) ((LValue) args[0]).getPointer().read();
			Type[] t = new Type[args.length-1];
			Pointer[] p = new Pointer[args.length-1];
			for(int i=1; i<args.length; i++) {
				t[i-1] = args[i].getType();
				p[i-1] = ((LValue) args[i]).getPointer();
			}
			DataRead.call(file, t, p);
			return null;
		}
	}
	
	public static final DataRead read = new DataRead();
	
	@Override
	public int getArgNum() {
		return 3;
	}

	@Override
	public Object call(Object[] args) {
		return null;
	}

	private static void call(DataFileObject file, Type[] types, Pointer[] ptrs) {
		try {
			DataInputStream in = file.getInputStream();
			for(int i=0; i<types.length; i++) {
				Object v = readData(in, types[i]);
				ptrs[i].write(v);
			}
		}
		catch(IOException e) {
			throw readError();
		}
	}
	
	public static Object readData(DataInputStream in, Type t) throws IOException {
		if(t==Type.integer)
			return in.readInt();
		else if(t==Type.real)
			return in.readDouble();
		else if(t==Type.bool)
			return in.readBoolean();
		else if(t==Type.character)
			return in.readChar();
		else if(t==Type.string)
			return in.readUTF();
		else if(t instanceof EnumType) {
			EnumType et = (EnumType) t;
			return et.getOrdinator().unord(in.readInt());
		}
		else if(t instanceof RangeType) {
			RangeType rt = (RangeType) t;
			Object v = readData(in, rt.getBaseType());
			if(!rt.range.check(v))
				JPasError.rangeCheckError();
			return v;
		}
		else if(t instanceof ArrayType) {
			ArrayType at = (ArrayType) t;
			int len = at.range.length();
			Object[] v = new Object[len];
			for(int i=0; i<len; i++)
				v[i] = readData(in, at.type);
			return new ArrayObject(at.range, v);
		}
		else if(t instanceof RecordType) {
			RecordType rt = (RecordType) t;
			List<Type> members = rt.memberTypes();
			int len = members.size();
			Object[] v = new Object[len];
			for(int i=0; i<len; i++)
				v[i] = readData(in, members.get(i));
			return new RecordObject(members, v);
		}
		else
			return t.init(null);
	}
	

}
