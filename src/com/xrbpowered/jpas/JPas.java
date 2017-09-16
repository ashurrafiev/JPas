package com.xrbpowered.jpas;

import java.io.File;
import java.io.IOException;

import com.xrbpowered.jpas.ast.Statement;

public class JPas extends Thread {

	private final Statement code;
	
	public JPas(Statement code) {
		this.code = code;
	}
	
	@Override
	public void run() {
		code.execute();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		if(args.length<1) {
			System.err.println("Input file?");
			System.exit(1);
		}
		
		JPasParser parser = new JPasParser();
		Statement code = null;
		try {
			code = parser.parse(new File(args[0]));
			if(code==null) {
				System.err.println("Compiled with errors.");
				System.exit(1);
			}
		} catch(IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		
		new JPas(code).start();
	}

}
