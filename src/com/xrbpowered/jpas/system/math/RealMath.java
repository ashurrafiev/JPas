package com.xrbpowered.jpas.system.math;

import com.xrbpowered.jpas.Scope;
import com.xrbpowered.jpas.ast.Type;
import com.xrbpowered.jpas.ast.exp.Function;

public abstract class RealMath extends Function {

	private static final Type[] argTypes = {Type.real};
	
	@Override
	public Type getType() {
		return Type.real;
	}
	
	@Override
	public Type[] getArgTypes() {
		return argTypes;
	}
	
	public static void register(Scope global) {
		
		global.addFunction("arctan", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.atan((Double) args[0]));
			}
		});
		
		global.addFunction("sin", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.sin((Double) args[0]));
			}
		});
		
		global.addFunction("cos", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.cos((Double) args[0]));
			}
		});
		
		global.addFunction("exp", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.exp((Double) args[0]));
			}
		});

		global.addFunction("ln", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.log((Double) args[0]));
			}
		});
		
		global.addFunction("sqrt", new RealMath() {
			@Override
			public Object call(Object[] args) {
				return new Double(Math.sqrt((Double) args[0]));
			}
		});

		global.addFunction("frac", new RealMath() {
			@Override
			public Object call(Object[] args) {
				double x = Math.abs((Double) args[0]);
				return new Double(x - Math.floor(x));
			}
		});

	}
}
