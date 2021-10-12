package com.xrbpowered.jpas.system.io.dir;

import java.io.File;

import com.xrbpowered.jpas.system.io.FileProc;

public class RemoveDir extends ChDir {

	@Override
	public Object call(Object[] args) {
		File f = getDir(args); 
		try {
			if(f.delete())
				return null;
		}
		catch(Exception e) {
		}
		throw FileProc.accessDenied();
	}
	
}
