package com.lzw.tool;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyOutputStream extends ObjectOutputStream {

	public MyOutputStream(OutputStream temp) throws IOException, SecurityException {
		super(temp);
	}
	
	protected void writeStreamHeader() throws IOException {
        super.reset();
    }
}
