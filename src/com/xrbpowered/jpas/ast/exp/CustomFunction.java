package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.ast.data.FunctionDeclaration;
import com.xrbpowered.jpas.ast.data.FunctionDeclaration.ArgDef;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;
import com.xrbpowered.jpas.mem.StackFrameDesc;

public class CustomFunction extends Function {

	private StackFrameDesc sf = null;
	private Variable result = null;
	
	private final FunctionDeclaration decl;
	public Scope forwardScope = null;
	public Statement body = null;
	
	public CustomFunction(FunctionDeclaration decl) {
		this.decl = decl;
	}
	
	@Override
	public EntryType getScopeEntryType() {
		return decl.type==null ? EntryType.procedure : EntryType.function;
	}
	
	public Scope createScope(Scope parent) {
		Scope s = new Scope(parent);
		sf = s.stackFrame;
		if(decl.type!=null)
			result = (Variable) s.add("Result", new Variable(decl.type, s.stackFrame));
		if(decl.argDefs!=null) {
			for(ArgDef arg : decl.argDefs) {
				arg.var = (Variable) s.add(arg.name, arg.lvalue ? new RefArgument(arg.type, s.stackFrame) : new Variable(arg.type, s.stackFrame));
			}
		}
		return s;
	}
	
	@Override
	public boolean checkImpl() {
		return body!=null;
	}
	
	public StackFrameDesc getStackFrame() {
		return sf;
	}
	
	public Constant makeFuncRef() {
		return new Constant(new FunctionType(decl), this);
	}

	@Override
	public Type getType() {
		return decl.type;
	}
	
	@Override
	public boolean hasSideEffects() {
		return true;
	}

	@Override
	public int getArgNum() {
		return decl.argDefs==null ? 0 : decl.argDefs.size();
	}
	
	@Override
	public boolean isLValue(int argIndex) {
		return decl.argDefs.get(argIndex).lvalue;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return decl.argDefs.get(argIndex).type;
	}

	@Override
	public Object call(Object[] args) {
		sf.alloc();
		if(result!=null)
			result.init(null);
		if(args!=null) {
			for(int i=0; i<args.length; i++) {
				ArgDef arg = decl.argDefs.get(i);
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
		else {
			sf.release();
			return null;
		}
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
	
	public CustomFunction implement(FunctionDeclaration decl) {
		if(decl.argDefs==null && decl.type==null)
			return this;
		if(!this.decl.equals(decl))
			return null;
		this.decl.updateArgs(decl.argDefs);
		return this;
	}
	
}
