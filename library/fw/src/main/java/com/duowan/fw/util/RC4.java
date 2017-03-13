package com.duowan.fw.util;

public class RC4 {
	private final static int STATE_LENGTH = 256;
	private byte[]	engineState = null;
    private int	x = 0;
    private int	y = 0;
    private byte[] workingKey = null;
    
    public RC4(byte[] key) {
    	this.setKey(key);
    }
    
	public void setKey(byte[] key) {
		workingKey = key;
		x = 0;
		y = 0;
		if (engineState == null) {
			engineState = new byte[STATE_LENGTH];
		}
		// reset the state of the engine
		for (int i = 0; i < STATE_LENGTH; i++) {
			engineState[i] = (byte) i;
		}
		int i1 = 0;
		int i2 = 0;
		for (int i = 0; i < STATE_LENGTH; i++) {
			i2 = ((key[i1] & 0xff) + engineState[i] + i2) & 0xff;
			// do the byte-swap inline
			byte tmp = engineState[i];
			engineState[i] = engineState[i2];
			engineState[i2] = tmp;
			i1 = (i1 + 1) % key.length;
		}
	}
	public void doFinal(byte[] in) {
		doFinal(in, 0, in.length);
	}
	public void doFinal(byte[] in, int start, int count) {
		for (int i = start; i < start + count; i++) {
			x = (x + 1) & 0xff;
			y = (engineState[x] + y) & 0xff;
			// swap
			byte tmp = engineState[x];
			engineState[x] = engineState[y];
			engineState[y] = tmp;
			// xor
			in[i] = (byte) (in[i] ^ engineState[(engineState[x] + engineState[y]) & 0xff]);
		}
	}
	
	public void reset() {
		this.setKey(workingKey);
	}
	
}
