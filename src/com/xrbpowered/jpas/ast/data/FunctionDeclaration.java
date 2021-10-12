package com.xrbpowered.jpas.ast.data;

import java.util.List;

import com.xrbpowered.jpas.ast.exp.Variable;

public class FunctionDeclaration {

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
		
		@Override
		public boolean equals(Object obj) {
			ArgDef def = (ArgDef) obj;
			return lvalue==def.lvalue && Type.checkEqual(type, def.type);
		}
	}
	
	public final List<ArgDef> argDefs;
	public final Type type;

	public FunctionDeclaration(List<ArgDef> args, Type type) {
		this.argDefs = args;
		this.type = type;
		// FIXME FunctionType as a result is not allowed
	}
	
	public int numArgs() {
		return argDefs==null ? 0 : argDefs.size();
	}
	
	public void updateArgs(List<ArgDef> args) {
		argDefs.clear();
		argDefs.addAll(args);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof FunctionDeclaration) {
			FunctionDeclaration prev = (FunctionDeclaration) obj;
			if(!Type.checkEqual(type, prev.type))
				return false;
			if((argDefs==null || argDefs.size()==0) && prev.argDefs!=null && prev.argDefs.size()>0)
				return false;
			if(argDefs!=null) {
				if(argDefs.size()!=prev.argDefs.size())
					return false;
				for(int i=0; i<argDefs.size(); i++)
					if(!argDefs.get(i).equals(prev.argDefs.get(i)))
						return false;
			}
			return true;
		}
		else
			return false;
	}
	
}
