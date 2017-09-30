package com.xrbpowered.jpas.ast.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.xrbpowered.jpas.JPasError;

public abstract class FileObject {

	private File file = null;
	
	public File getFile() {
		if(file==null)
			throw new JPasError("File is unassigned");
		return file;
	}
	
	public void assign(String path) {
		closeIfOpen();
		file = new File(path);
	}
	
	public abstract boolean isEof();
	public abstract boolean isOpen();
	public abstract void closeIfOpen();
	public abstract void flush();
	
	public InputStream openRead() {
		try {
			closeIfOpen();
			return new FileInputStream(getFile());
		} catch(IOException e) {
			throw new JPasError("Cannot open file for reading");
		}
	}
	
	public OutputStream openWrite(boolean append) {
		try {
			closeIfOpen();
			return new FileOutputStream(getFile(), append);
		} catch(IOException e) {
			throw new JPasError("Cannot open file for writing");
		}
	}
	
}
