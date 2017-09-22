package com.xrbpowered.jpas;

public class JPasError extends RuntimeException {

	public JPasError(String msg) {
		super(msg);
	}
	
	public JPasError get() {
		return new JPasError(getMessage());
	}

	public static final JPasError rangeCheckError = new JPasError("Range check error.");
	
}
