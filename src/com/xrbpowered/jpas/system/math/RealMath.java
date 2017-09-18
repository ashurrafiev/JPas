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
		
		global.add("arctan", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.atan((Double) args[0]));
			}
		});
		
		global.add("sin", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.sin((Double) args[0]));
			}
		});
		
		global.add("cos", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.cos((Double) args[0]));
			}
		});
		
		global.add("exp", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.exp((Double) args[0]));
			}
		});

		global.add("ln", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.log((Double) args[0]));
			}
		});
		
		global.add("sqrt", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.sqrt((Double) args[0]));
			}
		});

		global.add("frac", new RealMath() {
			@Override
			public Object call(Object[] args) {
				double x = Math.abs((Double) args[0]);
				return new Double(x - Math.floor(x));
			}
		});

	}
}
