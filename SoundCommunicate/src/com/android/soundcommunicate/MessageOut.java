package com.android.soundcommunicate;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class MessageOut {
	private boolean msgIsSending = false;
	private final String TAG = "SoundPlay";
	
	/*此buffer的大小影响到发送数据缓存区的大小，
	 * 发送数据缓存区大小计算: 
	 * 
	 */
	public static int msgMinBufferSize = AudioTrack.getMinBufferSize(EncoderCore.getMsgSamplerate(),
																	 AudioFormat.CHANNEL_OUT_STEREO,
																	 AudioFormat.ENCODING_PCM_8BIT)*5;

	AudioTrack msgAT;
	
	public boolean msgIsSending(){
		return msgIsSending;
	}
	
	public void msgStart(byte[] carrierSignal) {
		if(msgIsSending)
			msgStop();
		
		msgAT = new AudioTrack(AudioManager.STREAM_MUSIC,
				EncoderCore.getMsgSamplerate(),  
				AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_8BIT, 
				msgMinBufferSize,
				AudioTrack.MODE_STATIC);	
		
	   Log.d(TAG, EncoderCore.getMsgSamplerate()+"---------------");
	   Log.d(TAG,msgMinBufferSize+"------------");
		
		
		msgIsSending = true;
		
		System.out.println("--------"+carrierSignal[0]+"---"+EncoderCore.getEncoderBufferSize());
		
		msgAT.write(carrierSignal, 0, EncoderCore.getEncoderBufferSize());
		System.out.println("--------"+carrierSignal[0]+"---"+EncoderCore.getEncoderBufferSize());
		for(int i=0;i>carrierSignal.length;i++){
			System.out.println("--------"+carrierSignal[i]+"---"+EncoderCore.getEncoderBufferSize());
		}
		
		msgAT.flush();
		msgAT.setStereoVolume(0, 1);
		//msgAT.setLoopPoints(0, EncoderCore.getEncoderBufferSize(), -1); 
		
		msgAT.play();
		
		
	}

	public void msgStop() {
		if(msgAT != null)
		{
			msgAT.release();
			msgAT = null;
		}
		msgIsSending = false;
	}
}
