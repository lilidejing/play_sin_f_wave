package com.android.soundcommunicate;

public class DecoderCore {
	static{

		System.loadLibrary("encoder_decoder_core");
	}

	public native String decoderProcess(short[] inBuf);  
	
	public native static int getRecordSampleRate();
}//DecodeCore end
