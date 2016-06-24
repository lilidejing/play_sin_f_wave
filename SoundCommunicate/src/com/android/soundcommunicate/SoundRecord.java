package com.android.soundcommunicate;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class SoundRecord {
	private static final String TAG = "MyAudioRecord";
	private final int recSampleRate = DecoderCore.getRecordSampleRate();
	private final int recChannel = AudioFormat.CHANNEL_CONFIGURATION_MONO;  
	private final int recAudioFormat = AudioFormat.ENCODING_PCM_16BIT; 
	private final int audioSource = MediaRecorder.AudioSource.MIC;

	private boolean recordFlag = false;
	
	AudioRecord audioRecord;//录音
	RecordThread recThread;
	DecoderCore dc;
	
	public int minRecBufSize = AudioRecord.getMinBufferSize(recSampleRate, recChannel, recAudioFormat);
	
	public boolean isRecording(){
		return recordFlag;
	}
	
	public SoundRecord(){
		audioRecord = new AudioRecord(audioSource, recSampleRate, recChannel, recAudioFormat, minRecBufSize);
		dc = new DecoderCore();
	}

	public void start(){
		recThread = new RecordThread(audioRecord, minRecBufSize);
		
		recordFlag = true;
		recThread.start();
	}
	
	public void stop(){
		recordFlag = false; 
		recThread = null;
	}
	
	public class RecordThread extends Thread{
		private AudioRecord ar;
		private int bufSize;
		
		public RecordThread(AudioRecord audioRecord, int bufferSize){
			this.ar = audioRecord;
			this.bufSize = bufferSize;
		}
		
		public void run(){
			try{ 
				short[] buffer = new short[bufSize];
				//int 
				
				ar.startRecording();
				
				while(recordFlag){
					int ret = ar.read(buffer, 0, bufSize);
					
					if(ret == AudioRecord.ERROR_BAD_VALUE){
						recordFlag = false;
					}
					else{
						short[] tmpBuf = new short[ret];
						System.arraycopy(buffer, 0, tmpBuf, 0, ret);
						String msgToken = dc.decoderProcess(tmpBuf);
						
						msgHandOut(msgToken);
					}
					
				}//while end   
				ar.stop();
			}//try end
			catch (Exception e) {
				Log.e("Receive message E",e.toString());
			}
		}//run end
	}//RecordThread end

	public void msgHandOut(String msgToken) {
		
	}
}//MyAudioRecord end
