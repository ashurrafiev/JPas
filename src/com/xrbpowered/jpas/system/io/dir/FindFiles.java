package com.xrbpowered.jpas.system.io.dir;

import java.io.File;

import com.xrbpowered.jpas.JPas;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.ArrayObject;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.PointerType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.mem.FreePointer;
import com.xrbpowered.jpas.mem.Pointer;
import com.xrbpowered.jpas.system.NewPtrArray;

public class FindFiles extends Function {

	public static class Call extends Function.Call {

		public Call(Function.Call call) {
			super(call);
		}
		
		@Override
		public Object evaluate() {
			return ((FindFiles) f).call((PointerType) args[0].getType(), ((LValue) args[0]).getPointer());
		}
	}
	
	private final boolean dirs;
	
	public FindFiles(boolean dirs) {
		this.dirs = dirs;
	}
	
	@Override
	public Type getType() {
		return Type.integer;
	}

	@Override
	public int getArgNum() {
		return 1;
	}

	@Override
	public boolean isLValue(int argIndex) {
		return true;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return new PointerType(new ArrayType(null, Type.string));
	}
	
	protected boolean matches(File f) {
		if(dirs)
			return f.isDirectory();
		else
			return f.isFile();
	}
	
	private int call(PointerType type, Pointer ptr) {
		File[] files = JPas.workingDir.listFiles();
		int count = 0;
		for(File f : files) {
			if(matches(f))
				count++;
		}
		if(count==0) {
			ptr.write(null);
			return 0;
		}
		else {
			Object[] v = new Object[count];
			int i = 0;
			for(File f : files) {
				if(matches(f)) {
					v[i] = f.getName();
					i++;
				}
			}
			
			ArrayType t = (ArrayType) NewPtrArray.makeType(type.getType(), new int[] {count}, 0);
			ptr.write(new FreePointer(t, new ArrayObject(t.range, v)));
			return count;
		}
	}
	
	@Override
	public Object call(Object[] args) {
		return null;
	}
	
	@Override
	public Function.Call makeCall(Scope scope, Expression[] args) {
		return new FindFiles.Call(super.makeCall(scope, args));
	}

}
