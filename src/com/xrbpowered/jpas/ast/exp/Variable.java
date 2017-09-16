package com.xrbpowered.jpas.ast.exp;

import com.xrbpowered.jpas.Scope.EntryType;
import com.xrbpowered.jpas.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.ast.Type;

public class Variable extends LValue implements ScopeEntry {

	public static class VarInit extends Statement {
		private final Variable[] vars;
		private final Expression def;
		
		public VarInit(Variable[] vars, Expression def) {
			this.vars = vars;
			this.def = def;
		}
		
		@Override
		public void execute() {
			Object defv = def==null ? vars[0].type.getDefValue() : def.evaluate(); 
			for(Variable v : vars)
				v.value = defv;
		};
	}
	
	private final Type type;
	private Object value;
	
	public Variable(Type type) {
		this.type = type;
	}
	
	@Override
	public EntryType getScopeEntryType() {
		return EntryType.variable;
	}
	
	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	public void assign(Object val) {
		value = type.assign(value, val);
	}

	@Override
	public Object evaluate() {
		return value;
	}
	
	@Override
	public boolean isConst() {
		return false;
	}
	
}
