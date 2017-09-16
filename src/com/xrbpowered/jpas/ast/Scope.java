package com.xrbpowered.jpas.ast;

import java.util.HashMap;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.Variable;
import com.xrbpowered.jpas.mem.StackFrameDesc;
import com.xrbpowered.jpas.system.Delay;
import com.xrbpowered.jpas.system.Format;
import com.xrbpowered.jpas.system.Length;
import com.xrbpowered.jpas.system.Write;
import com.xrbpowered.jpas.system.math.Abs;
import com.xrbpowered.jpas.system.math.IntMath;
import com.xrbpowered.jpas.system.math.Max;
import com.xrbpowered.jpas.system.math.Min;
import com.xrbpowered.jpas.system.math.Odd;
import com.xrbpowered.jpas.system.math.Rand;
import com.xrbpowered.jpas.system.math.Randomize;
import com.xrbpowered.jpas.system.math.RealMath;
import com.xrbpowered.jpas.system.math.Sqr;

public class Scope {

	public interface ScopeEntry {
		public EntryType getScopeEntryType();
	};
	
	public static enum EntryType {
		variable, function, procedure, type, argument, result
	}
	
	public final StackFrameDesc stackFrame = new StackFrameDesc();
	public final Scope parent;
	private HashMap<String, ScopeEntry> idMap = new HashMap<>();

	public Scope(Scope parent) {
		this.parent = parent;
	}
	
	public ScopeEntry find(String name) {
		ScopeEntry e = idMap.get(name);
		if(e==null && parent!=null)
			e = parent.find(name);
		return e;
	}
	
	private void put(String name, ScopeEntry e) {
		if(find(name)!=null)
			throw new JPasError("Duplicate identifier: "+name);
		else
			idMap.put(name, e);
	}
	
	public Variable addVariable(String name, Type type) {
		Variable v = new Variable(type, stackFrame);
		put(name, v);
		return v;
	}
	
	public Function addFunction(String name, Function f) {
		put(name, f);
		return f;
	}

	public static Scope global() {
		Scope global = new Scope(null);
		
		global.addFunction("write", new Write(false));
		global.addFunction("writeln", new Write(true));
		global.addFunction("format", new Format());
		global.addFunction("delay", new Delay());
		
		RealMath.register(global);
		IntMath.register(global);
		global.addFunction("abs", new Abs());
		global.addFunction("sqr", new Sqr());
		global.addFunction("min", new Min());
		global.addFunction("max", new Max());
		global.addFunction("odd", new Odd());

		global.addFunction("randomize", new Randomize());
		global.addFunction("random", new Rand());
		global.addFunction("length", new Length());

		return global;
	}
	
}
