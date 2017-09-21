package com.xrbpowered.jpas.system;

import java.util.Scanner;

import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.mem.Pointer;

public class ReadLn extends Function {

	@Override
	public EntryType getScopeEntryType() {
		return EntryType.procedure;
	}
	
	@Override
	public Type getType() {
		return null;
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
		return Type.string;
	}

	@Override
	public Object call(Object[] args) {
		Scanner in = Read.getConsole();
		String line = in.nextLine();
		if(args!=null)
			((Pointer) args[0]).write(line);
		return null;
	}
	
	@Override
	public Call makeCall(Expression[] args) {
		if(args==null || args.length==0)
			return new Function.Call(this, null);
		else if(args.length==1 && args[0].getType()==Type.string)
			return super.makeCall(args);
		else
			return Read.readLn.makeCall(args);
	}

}
