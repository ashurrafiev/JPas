package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public abstract class RealMath extends Function {

	@Override
	public Type getType() {
		return Type.real;
	}
	
	@Override
	public int getArgNum() {
		return 1;
	}
	
	@Override
	public Type getArgType(int argIndex) {
		return Type.real;
	}

	public static void register(Scope global) {
		
		global.add("ArcTan", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.atan((Double) args[0]));
			}
		});
		
		global.add("Sin", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.sin((Double) args[0]));
			}
		});
		
		global.add("Cos", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.cos((Double) args[0]));
			}
		});
		
		global.add("Exp", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.exp((Double) args[0]));
			}
		});

		global.add("Ln", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.log((Double) args[0]));
			}
		});
		
		global.add("Sqrt", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.sqrt((Double) args[0]));
			}
		});

		global.add("Frac", new RealMath() {
			@Override
			public Object call(Object[] args) {
				double x = Math.abs((Double) args[0]);
				return new Double(x - Math.floor(x));
			}
		});

	}
}
