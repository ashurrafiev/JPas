package com.xrbpowered.jpas;

import java.io.File;
import java.io.IOException;

import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.parse.JPasParser;

public class JPas extends Thread {

	private static boolean verboseErrors = false;
	private static boolean timing = false;
	
	public static long execStarted = 0L;
	public static File workingDir;
	
	private final Statement code;
	
	public JPas(Statement code) {
		this.code = code;
	}
	
	private void showError(Exception e, String message) {
		if(verboseErrors)
			e.printStackTrace();
		else
			System.err.println("Runtime error: "+message);
		System.exit(1);
	}
	
	@Override
	public void run() {
		try {
			execStarted = System.currentTimeMillis();
			code.execute();
			if(timing)
				System.err.printf("Finished in %d ms.\n", System.currentTimeMillis()-execStarted);
			System.exit(0);
		}
		catch(JPasError e) {
			showError(e, e.getMessage());
		}
		catch(Exception e) {
			showError(e, "Internal system error, use -verr for details.");
		}
	}
	
	private static void printHelp() {
		System.out.println("Usage:");
		System.out.println("java -jas jpas.jar [options] inputfile");
		System.out.println();
		System.out.println("Options:");
		System.out.println("-time \t Print timing information about pre-compilation stage and program execution.");
		System.out.println("-verr \t Print Java stack traces on runtime errors (dev mode).");
		System.out.println("-help \t Print this message.");
	}
	
	public static Statement compile(JPasParser parser, File file) {
		Statement code = null;
		try {
			file = file.getAbsoluteFile();
			parser.workingDir = file.getParentFile();
			
			if(timing)
				System.err.printf("Compiling %s...\n", file.getName());
			long t = System.currentTimeMillis();
			code = parser.parse(file);
			if(timing)
				System.err.printf("Compiled in %d ms.\n", System.currentTimeMillis()-t);
			if(code==null) {
				System.err.println("Compiled with errors.");
				return null;
			}
		} catch(IOException e) {
			System.err.println(e.getMessage());
			return null;
		}
		return code;
	}
	
	public static void main(String[] args) {
		workingDir = new File(".");
		
		String in = null;
		boolean help = false;
		if(args.length<1)
			help = true;
		else {
			for(String arg : args) {
				if(arg.equalsIgnoreCase("-time"))
					timing = true;
				else if(arg.equalsIgnoreCase("-verr"))
					verboseErrors = true;
				else if(arg.equalsIgnoreCase("-help"))
					help = true;
				else
					in = arg;
			}
		}
		
		if(help) {
			printHelp();
			System.exit(1);
		}
		
		if(in==null) {
			System.err.println("Input file?");
			System.exit(1);
		}
		
		JPasParser parser = new JPasParser();
		Statement code = compile(parser, new File(in));
		if(code!=null)
			new JPas(code).start();
		else
			System.exit(1);
	}

}
