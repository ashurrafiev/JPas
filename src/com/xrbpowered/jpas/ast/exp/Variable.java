package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.mem.Pointer;
import com.xrbpowered.jpas.mem.StackFrameDesc;
import com.xrbpowered.jpas.mem.StackFrameObject;

public class Variable extends LValue implements ScopeEntry, StackFrameObject {

	public static class VarInit extends Statement {
		private final Variable[] vars;
		private final Expression def;
		
		public VarInit(Variable[] vars, Expression def) {
			this.vars = vars;
			this.def = def;
		}
		
		@Override
		public String execute() {
			Object defv = def==null ? null : def.evaluate(); 
			for(Variable v : vars)
				v.init(defv);
			return null;
		};
	}
	
	protected final Type type;
	protected final StackFrameDesc sf;
	protected final int sfIndex;
	
	public Variable(Type type, StackFrameDesc sf) {
		this.type = type;
		this.sf = sf;
		this.sfIndex = register(sf);
	}
	
	@Override
	public int register(StackFrameDesc sf) {
		if(sf==null)
			throw new JPasError("Outside of stack frame.");
		return sf.register(this);
	}
	
	@Override
	public EntryType getScopeEntryType() {
		return EntryType.variable;
	}
	
	@Override
	public boolean checkImpl() {
		return true;
	}
	
	@Override
	public Type getType() {
		return type;
	}

	@Override
	public void init(Object val) {
		sf.write(sfIndex, type.init(val));
	}

	@Override
	public void free() {
		type.free(getPointer());
	}
	

	@Override
	public Pointer getPointer() {
		return sf.getPointer(type, sfIndex);
	}
	
	@Override
	public Object evaluate() {
		return sf.read(sfIndex);
	}
	
	@Override
	public boolean isConst() {
		return false;
	}

}
