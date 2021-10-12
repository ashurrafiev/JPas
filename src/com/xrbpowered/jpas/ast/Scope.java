package com.xrbpowered.jpas.ast;

import java.util.LinkedList;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.exp.Constant;
import com.xrbpowered.jpas.mem.StackFrameDesc;
import com.xrbpowered.jpas.system.Delay;
import com.xrbpowered.jpas.system.Elapsed;
import com.xrbpowered.jpas.system.Fill;
import com.xrbpowered.jpas.system.Halt;
import com.xrbpowered.jpas.system.NewPtr;
import com.xrbpowered.jpas.system.NewPtrArray;
import com.xrbpowered.jpas.system.RunError;
import com.xrbpowered.jpas.system.Swap;
import com.xrbpowered.jpas.system.SysTime;
import com.xrbpowered.jpas.system.io.Assign;
import com.xrbpowered.jpas.system.io.BlockRead;
import com.xrbpowered.jpas.system.io.BlockWrite;
import com.xrbpowered.jpas.system.io.ByteRead;
import com.xrbpowered.jpas.system.io.ByteWrite;
import com.xrbpowered.jpas.system.io.Close;
import com.xrbpowered.jpas.system.io.Eof;
import com.xrbpowered.jpas.system.io.EraseFile;
import com.xrbpowered.jpas.system.io.FileSize;
import com.xrbpowered.jpas.system.io.Read;
import com.xrbpowered.jpas.system.io.ReadLn;
import com.xrbpowered.jpas.system.io.RenameFile;
import com.xrbpowered.jpas.system.io.Rewrite;
import com.xrbpowered.jpas.system.io.Write;
import com.xrbpowered.jpas.system.io.dir.ChDir;
import com.xrbpowered.jpas.system.io.dir.FindFiles;
import com.xrbpowered.jpas.system.io.dir.GetDir;
import com.xrbpowered.jpas.system.io.dir.MakeDir;
import com.xrbpowered.jpas.system.io.dir.RemoveDir;
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
import com.xrbpowered.jpas.system.ord.Ord;
import com.xrbpowered.jpas.system.ord.SuccPred;
import com.xrbpowered.jpas.system.str.Chr;
import com.xrbpowered.jpas.system.str.Concat;
import com.xrbpowered.jpas.system.str.Copy;
import com.xrbpowered.jpas.system.str.Delete;
import com.xrbpowered.jpas.system.str.Format;
import com.xrbpowered.jpas.system.str.Insert;
import com.xrbpowered.jpas.system.str.Length;
import com.xrbpowered.jpas.system.str.LowCase;
import com.xrbpowered.jpas.system.str.Pos;
import com.xrbpowered.jpas.system.str.Str;
import com.xrbpowered.jpas.system.str.UpCase;
import com.xrbpowered.jpas.system.str.Val;
import com.xrbpowered.jpas.units.StandardUnit;
import com.xrbpowered.jpas.units.graph2d.Graph2D;

public class Scope {

	public interface ScopeEntry {
		public EntryType getScopeEntryType();
		public boolean checkImpl();
	};
	
	public static enum EntryType {
		variable, function, procedure, type, unit
	}
	
	public final StackFrameDesc stackFrame;
	public final Scope parent;
	public final Scope forwardScope;
	private IdMap<ScopeEntry> idMap = new IdMap<>();
	
	public LinkedList<String> labels;
	
	public Scope(Scope parent) {
		this(parent, false);
	}
	
	public Scope(Scope parent, boolean expand) {
		this.parent = parent;
		if(parent==null)
			labels = new LinkedList<>();
		else
			labels = parent.labels;
		if(expand) {
			this.forwardScope = parent.forwardScope;
			this.stackFrame = parent.stackFrame;
		}
		else {
			this.forwardScope = this;
			this.stackFrame = new StackFrameDesc(parent==null ? null : parent.stackFrame);
		}
	}
	
	public void breakLabels() {
		this.labels = new LinkedList<>();
	}
	
	public void restoreLabels(LinkedList<String> labels) {
		this.labels = labels;
	}
	
	public void checkDefs() {
		for(IdMap<ScopeEntry>.IdEntry e : idMap.map.values())
			if(!e.e.checkImpl())
				throw new JPasError(e.trueName+" not implemented.");
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
	
	public boolean isUnitLoaded(String name) {
		ScopeEntry e = find(name);
		return e!=null && e.getScopeEntryType()==EntryType.unit;
	}

	public static Scope global() {
		Scope global = new Scope(null);

		global.add("Halt", new Halt());
		global.add("RunError", new RunError());

		global.add("New", new NewPtr());
		global.add("NewArray", new NewPtrArray());

		global.add("Assign", new Assign());
		global.add("Erase", new EraseFile());
		global.add("Rename", new RenameFile());
		global.add("FileSize", new FileSize());
		
		global.add("Reset", new Rewrite(false, false));
		global.add("Rewrite", new Rewrite(true, false));
		global.add("Append", new Rewrite(true, true));
		global.add("Flush", new Close(false));
		global.add("Close", new Close(true));
		global.add("Eof", new Eof());

		global.add("Write", new Write(false));
		global.add("WriteLn", new Write(true));
		global.add("Read", new Read(false));
		global.add("ReadLn", new ReadLn());
		global.add("BlockRead", new BlockRead());
		global.add("BlockWrite", new BlockWrite());
		global.add("ByteRead", new ByteRead());
		global.add("ByteWrite", new ByteWrite());

		global.add("GetDir", new GetDir());
		global.add("ChDir", new ChDir());
		global.add("RmDir", new RemoveDir());
		global.add("MkDir", new MakeDir());
		global.add("FindDirs", new FindFiles(true));
		global.add("FindFiles", new FindFiles(false));
		
		global.add("Delay", new Delay());
		global.add("SysTime", new SysTime());
		global.add("Elapsed", new Elapsed());
		
		global.add("Pi", Constant.constPi);
		RealMath.register(global);
		IntMath.register(global);
		global.add("Abs", new Abs());
		global.add("Sqr", new Sqr());
		global.add("Min", new Min());
		global.add("Max", new Max());
		global.add("Odd", new Odd());
		
		global.add("Inc", new IncDec(true));
		global.add("Dec", new IncDec(false));
		global.add("Succ", new SuccPred(true));
		global.add("Pred", new SuccPred(false));
		global.add("Ord", new Ord());
		
		global.add("Chr", new Chr());
		global.add("UpCase", new UpCase());
		global.add("LowCase", new LowCase());
		global.add("Format", new Format());
		global.add("Str", new Str());
		global.add("Val", new Val());
		global.add("Copy", new Copy());
		global.add("Length", new Length());
		global.add("Pos", new Pos());
		global.add("Concat", new Concat());
		global.add("Delete", new Delete());
		global.add("Insert", new Insert());
		// MAYBE: same functions for arrays
		global.add("Fill", new Fill());

		global.add("Randomize", new Randomize());
		global.add("Random", new Rand());
		global.add("Swap", new Swap());

		return global;
	}
	
	public static StandardUnit getStandardUnit(String name) {
		if(name.equalsIgnoreCase("Graph2D"))
			return Graph2D.unit;

		throw new JPasError("Unknown standard library unit.");
	}
	
}
