package com.xrbpowered.jpas.ast.exp;

import java.util.List;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;
import com.xrbpowered.jpas.mem.StackFrameDesc;

public class CustomFunction extends Function {

	public static class ArgDef {
		public final String name;
		public final Type type;
		public final boolean lvalue;
		public Variable var;
		
		public ArgDef(String name, Type type, boolean lvalue) {
			this.name = name;
			this.type = type;
			this.lvalue = lvalue;
		}
	}
	
	private StackFrameDesc sf = null;
	private Variable result = null;
	
	public Scope forwardScope = null;
	public Statement body = null;
	
	private final List<ArgDef> argDefs;
	private final Type type;
	
	public CustomFunction(List<ArgDef> args, Type type) {
		this.argDefs = args;
		this.type = type;
	}
	
	@Override
	public EntryType getScopeEntryType() {
		return type==null ? EntryType.procedure : EntryType.function;
	}
	
	public Scope createScope(Scope parent) {
		Scope s = new Scope(parent);
		sf = s.stackFrame;
		if(type!=null)
			result = s.addVariable("result", type);
		if(argDefs!=null) {
			for(ArgDef arg : argDefs) {
				arg.var = arg.lvalue ? s.addRefArgument(arg.name, arg.type) : s.addVariable(arg.name, arg.type);
			}
		}
		return s;
	}
	
	@Override
	public Type getType() {
		return type;
	}

	@Override
	public int getArgNum() {
		return argDefs==null ? 0 : argDefs.size();
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return argDefs.get(argIndex).lvalue;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return argDefs.get(argIndex).type;
	}

	@Override
	public Object call(Object[] args) {
		sf.alloc();
		if(result!=null)
			result.init(null);
		if(args!=null) {
			for(int i=0; i<args.length; i++) {
				ArgDef arg = argDefs.get(i);
				if(arg.lvalue)
					((RefArgument) arg.var).setPointer((Pointer) args[i]);
				else
					arg.var.init(args[i]);
			}
		}
		body.execute();
		if(result!=null) {
			Object res = result.evaluate();
			sf.release();
			return res;
		}
		else
			return null;
	}

	public static boolean isDuplicateId(ScopeEntry e, EntryType etype) {
		if(e==null)
			return false;
		if(e.getScopeEntryType()!=etype)
			return true;
		if(!(e instanceof CustomFunction))
			return true;
		return ((CustomFunction) e).body!=null; 
	}
	
}
