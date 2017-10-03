package com.xrbpowered.jpas.ast.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.xrbpowered.jpas.JPasError;

public class TextFileObject extends FileObject {

	private boolean std = false;
	private Scanner textIn = null;
	private PrintStream textOut = null;

	@Override
	public void assign(String path) {
		if(path.isEmpty()) {
			std = true;
		}
		else {
			std = false;
			super.assign(path);
		}
	}
	
	@Override
	public boolean isOpen() {
		return std || textIn!=null || textOut!=null;
	}

	@Override
	public void closeIfOpen() {
		if(textIn!=null) {
			if(!std)
				textIn.close();
			textIn = null;
		}
		if(textOut!=null) {
			if(!std)
				textOut.close();
			textOut = null;
		}
	}
	
	@Override
	public InputStream openRead() {
		InputStream in = std ? System.in : super.openRead();
		textIn = new Scanner(in);
		return in;
	}
	
	@Override
	public OutputStream openWrite(boolean append) {
		return textOut = (std ? System.out : new PrintStream(super.openWrite(append)));
	}
	
	public Scanner getScanner() {
		if(textIn==null)
			throw new JPasError("File is not open for reading");
		return textIn;
	}

	public PrintStream getPrintWriter() {
		if(textOut==null)
			throw new JPasError("File is not open for writing");
		return textOut;
	}

	@Override
	public boolean isEof() {
		return !getScanner().hasNext() && !getScanner().hasNextLine();
	}
	
	@Override
	public void flush() {
		getPrintWriter().flush();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj)
			return true;
		if(obj instanceof TextFileObject) {
			TextFileObject f = (TextFileObject) obj;
			if(std || f.std)
				return std && f.std;
			else
				return super.equals(obj);
		}
		return false;
	}

}
