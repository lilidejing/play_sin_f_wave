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
	/** ���� **/
	float volume;
	/** ���� **/
	int channel;
	/** �ܳ��� **/
	int length;
	/** һ�����Ҳ��ĳ��� **/
	int waveLen;
	/** Ƶ�� **/
	int Hz;
	/** ���Ҳ� **/
	byte[] wave;

	public AudioTrackManager() {
		wave = new byte[RATE];
	}

	/**
	 * ����Ƶ��
	 * 
	 * @param rate
	 */
	public void start(int rate) {
		stop();
		if (rate > 0) {
			Hz = rate;
			waveLen = RATE / Hz;// ÿ�����Ҳ��ĳ���
			length = waveLen * Hz;
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					RATE,
					AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_8BIT, length,
					AudioTrack.MODE_STREAM);
			audioTrack.setStereoVolume(1, 0);
			// �������Ҳ�
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
	 * ƴ��buffer
	 * @author json_data
	 * @param byteBuffer1,byteBuffer2Ҫƴ�ӵ�buffer
	 * @return byteBufferTemp,�ϳɺ��buffer
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
	 * д������
	 */
	public void play() {
		if (audioTrack != null) {
			audioTrack.write(wave, 0, length);
		}
	}

	/**
	 * ֹͣ����
	 */
	public void stop() {
		if (audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
			audioTrack = null;
		}
	}

	/**
	 * ��������
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
	 * ��������
	 * 
	 * @param channel
	 */
	public void setChannel(int channel) {
		this.channel = channel;
		setVolume(volume);
	}
}
