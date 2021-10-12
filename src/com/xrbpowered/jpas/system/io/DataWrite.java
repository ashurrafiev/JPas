package com.xrbpowered.jpas.system.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

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

public class DataWrite extends IOProc {

	public static class Call extends Function.Call {
		public Call(Function f, Expression[] args) {
			super(f, args);
		}
		@Override
		public Object evaluate() {
			DataFileObject file = (DataFileObject) ((LValue) args[0]).getPointer().read();
			Type[] t = new Type[args.length-1];
			Object[] v = new Object[args.length-1];
			for(int i=1; i<args.length; i++) {
				t[i-1] = args[i].getType();
				v[i-1] = args[i].evaluate();
			}
			DataWrite.call(file, t, v);
			return null;
		}
	}
	
	public static final DataRead write = new DataRead();
	
	@Override
	public int getArgNum() {
		return 3;
	}

	@Override
	public Object call(Object[] args) {
		return null;
	}
	
	private static void call(DataFileObject file, Type[] types, Object[] v) {
		try {
			DataOutputStream out = file.getOutputStream();
			for(int i=0; i<v.length; i++) {
				writeData(out, types[i], v[i]);
			}
		}
		catch(IOException e) {
			throw readError();
		}
	}

	public static void writeData(DataOutputStream out, Type t, Object obj) throws IOException {
		if(t==Type.integer)
			out.writeInt((Integer) obj);
		else if(t==Type.real)
			out.writeDouble((Double) obj);
		else if(t==Type.bool)
			out.writeBoolean((Boolean) obj);
		else if(t==Type.character)
			out.writeChar((Character) obj);
		else if(t==Type.string)
			out.writeUTF((String) obj);
		else if(t instanceof EnumType) {
			EnumType et = (EnumType) t;
			out.writeInt(et.getOrdinator().ord(obj));
		}
		else if(t instanceof RangeType) {
			RangeType rt = (RangeType) t;
			writeData(out, rt.getBaseType(), obj);
		}
		else if(t instanceof ArrayType) {
			ArrayType at = (ArrayType) t;
			Object[] v = ((ArrayObject) obj).getValues();
			for(int i=0; i<v.length; i++)
				writeData(out, at.type, v[i]);
		}
		else if(t instanceof RecordType) {
			RecordType rt = (RecordType) t;
			List<Type> members = rt.memberTypes();
			for(int i=0; i<members.size(); i++)
				writeData(out, members.get(i), ((RecordObject) obj).get(i));
		}
	}
}
