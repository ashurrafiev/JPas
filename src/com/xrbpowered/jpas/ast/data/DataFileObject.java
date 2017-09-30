package com.xrbpowered.jpas.ast.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.xrbpowered.jpas.JPasError;

public class DataFileObject extends FileObject {

	private DataInputStream dataIn = null;
	private DataOutputStream dataOut = null;
	
	@Override
	public boolean isOpen() {
		return dataIn!=null || dataOut!=null;
	}
	
	@Override
	public void closeIfOpen() {
		if(dataIn!=null) {
			try {
				dataIn.close();
			} catch(IOException e) {
			}
			dataIn = null;
		}
		if(dataOut!=null) {
			try {
				dataOut.close();
			} catch(IOException e) {
			}
			dataOut = null;
		}
	}
	
	@Override
	public InputStream openRead() {
		return dataIn = new DataInputStream(super.openRead());
	}
	
	@Override
	public OutputStream openWrite(boolean append) {
		return dataOut = new DataOutputStream(super.openWrite(append));
	}
	
	public DataInputStream getInputStream() {
		if(dataIn==null)
			throw new JPasError("File is not open for reading");
		return dataIn;
	}

	public DataOutputStream getOutputStream() {
		if(dataOut==null)
			throw new JPasError("File is not open for writing");
		return dataOut;
	}

	@Override
	public boolean isEof() {
		try {
			return getInputStream().available()<=0;
		} catch(IOException e) {
			return true;
		}
	}
	
	@Override
	public void flush() {
		try {
			getOutputStream().flush();
		} catch(IOException e) {
		}
	}
	
}
