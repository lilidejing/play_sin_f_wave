package com.csw.soundcom;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class AudioTrackManager {
	public static final int RATE = 44100;
	public static final float MAXVOLUME = 100f;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int DOUBLE = 3;

	private static String TAG = "AudioTrackManager";
	
	AudioTrack audioTrack;
	/** 音量 **/
	float volume;
	/** 声道 **/
	int channel;
	/** 总长度 **/
	int length;
	/** 一个正弦波的长度 **/
	int waveLen;
	/** 频率 **/
	int Hz;
	/** 正弦波 **/
	byte[] wave;

	public AudioTrackManager() {
		wave = new byte[RATE];
	}

	/**
	 * 设置频率
	 * 
	 * @param rate
	 */
	public void start(int rate) {
		stop();
		if (rate > 0) {
			Hz = rate;
			waveLen = RATE / Hz;// 每段正弦波的长度
			length = waveLen * Hz;
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					RATE,
					AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_8BIT, length,
					AudioTrack.MODE_STREAM);
			audioTrack.setStereoVolume(1, 0);
			// 生成正弦波
			wave = SinWave.sin(wave, waveLen, length);
			if (audioTrack != null) {
				audioTrack.play();
			}
			Log.d(TAG, "waveLen=" + waveLen + ";wave=" + wave
					+ ";length=" + length);
			
			for(int i=0;i<wave.length;i++){
				Log.d(TAG, "waveLen=" + waveLen + ";wave=" + wave[i]);
			}
		} else {
			return;
		}

	}

	
	
	/**
	 * 拼接buffer
	 * @author json_data
	 * @param byteBuffer1,byteBuffer2要拼接的buffer
	 * @return byteBufferTemp,合成后的buffer
	 */
	
	public byte [] getAppendBuffer(byte[] byteBuffer1,byte[] byteBuffer2){
		 int byteBuffer1_length=byteBuffer1.length;
		 int byteBuffer2_length=byteBuffer2.length;
		  byte[] byteBufferTemp = new byte[byteBuffer1_length+byteBuffer1_length];
		  System.arraycopy(byteBuffer1,0,byteBufferTemp,0,byteBuffer1_length);
		  System.arraycopy(byteBuffer2,0,byteBufferTemp,byteBuffer1_length,byteBuffer2_length);
		  return byteBufferTemp;
		
	}
	
	
	
	/**
	 * 写入数据
	 */
	public void play() {
		if (audioTrack != null) {
			audioTrack.write(wave, 0, length);
		}
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		if (audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
			audioTrack = null;
		}
	}

	/**
	 * 设置音量
	 * 
	 * @param volume
	 */
	public void setVolume(float volume) {
		this.volume = volume;
		if (audioTrack != null) {
			switch (channel) {
			case LEFT:
				audioTrack.setStereoVolume(volume / MAXVOLUME, 0f);
				break;
			case RIGHT:
				audioTrack.setStereoVolume(0f, volume / MAXVOLUME);
				break;
			case DOUBLE:
				audioTrack.setStereoVolume(volume / MAXVOLUME, volume
						/ MAXVOLUME);
				break;
			}
		}
	}

	/**
	 * 设置声道
	 * 
	 * @param channel
	 */
	public void setChannel(int channel) {
		this.channel = channel;
		setVolume(volume);
	}
}
