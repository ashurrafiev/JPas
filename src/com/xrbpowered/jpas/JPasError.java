package com.xrbpowered.jpas;

public class JPasError extends RuntimeException {

	public JPasError(String msg) {
		super(msg);
	}

	public static JPasError rangeCheckError() {
		return new JPasError("Range check error");
	}
	
	public static JPasError argumentTypeError() {
		return new JPasError("Argument type mismatch");
	}

	public static JPasError lvalueError() {
		return new JPasError("Expected LValue");
	}
	
	public static JPasError argumentNumberError(boolean exceed) {
		return new JPasError(exceed ? "Too many arguments" : "Too few arguments");
	}

}
