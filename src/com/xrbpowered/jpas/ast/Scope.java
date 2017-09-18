package com.xrbpowered.jpas.ast;

import com.xrbpowered.jpas.ast.exp.Constant;
import com.xrbpowered.jpas.mem.StackFrameDesc;
import com.xrbpowered.jpas.system.Delay;
import com.xrbpowered.jpas.system.Format;
import com.xrbpowered.jpas.system.Length;
import com.xrbpowered.jpas.system.NewPtr;
import com.xrbpowered.jpas.system.Read;
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
import com.xrbpowered.jpas.system.ord.IncDec;

public class Scope {

	public interface ScopeEntry {
		public EntryType getScopeEntryType();
	};
	
	public static enum EntryType {
		variable, function, procedure, type
	}
	
	public final StackFrameDesc stackFrame = new StackFrameDesc();
	public final Scope parent;
	private IdMap<ScopeEntry> idMap = new IdMap<>();

	public Scope(Scope parent) {
		this.parent = parent;
	}
	
	private IdMap<ScopeEntry>.IdEntry findEx(String name) {
		IdMap<ScopeEntry>.IdEntry e = idMap.get(name);
		if(e==null && parent!=null)
			e = parent.findEx(name);
		return e;
	}
	
	public ScopeEntry find(String name) {
		IdMap<ScopeEntry>.IdEntry e = findEx(name.toLowerCase());
		return e==null ? null : e.e;
	}
	
	public String findTrueName(String name) {
		IdMap<ScopeEntry>.IdEntry e = findEx(name.toLowerCase());
		return e==null ? null : e.trueName;
	}
	
	public ScopeEntry add(String name, ScopeEntry e) {
		idMap.put(name, e);
		return e;
	}

	public static Scope global() {
		Scope global = new Scope(null);

		global.add("New", new NewPtr());

		global.add("Write", new Write(false));
		global.add("WriteLn", new Write(true));
		global.add("ReadLn", new Read());
		global.add("Format", new Format());
		global.add("Delay", new Delay());
		
		global.add("Pi", Constant.constPi);
		RealMath.register(global);
		IntMath.register(global);
		global.add("Abs", new Abs());
		global.add("Sqr", new Sqr());
		global.add("Min", new Min());
		global.add("Max", new Max());
		global.add("Odd", new Odd());
		// TODO ord functions
		global.add("Inc", new IncDec(true));
		global.add("Dec", new IncDec(false));
		// TODO string functions

		global.add("Randomize", new Randomize());
		global.add("Random", new Rand());
		global.add("Length", new Length());

		return global;
	}
	
}
