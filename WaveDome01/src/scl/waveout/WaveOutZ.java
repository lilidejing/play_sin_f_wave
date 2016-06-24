package scl.waveout;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class WaveOutZ {
	private int sampleRateInHz = 44100; // 采样率
	private int mChannel = AudioFormat.CHANNEL_CONFIGURATION_MONO;// 声道 ：单声道
	private int mSampBit = AudioFormat.ENCODING_PCM_16BIT;// 采样精度 :16bit

	// private short m_iAmp = Short.MAX_VALUE;
	// private short m_bitDateZ[] = new short[44100];
	// private double x = 2.0 * Math.PI * 8250.0 / 44100.0;
//	private boolean isRuning = false;
	private AudioTrack audioTrackz;

	public WaveOutZ() {

		// for (int i = 0; i < 44100; i++) {
		// m_bitDateZ[i] = (short) (m_iAmp * Math.sin(x * i));
		// }

		int bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, mChannel,
				mSampBit);
		audioTrackz = new AudioTrack(AudioManager.STREAM_SYSTEM,
				sampleRateInHz, mChannel, mSampBit, bufferSize * 2,
				AudioTrack.MODE_STREAM);
	}

	public void palyWaveZ() {
		new AudioTrackZThread().start();
	}

	public void colseWaveZ() {
		if (audioTrackz != null) {
			audioTrackz.stop();
			audioTrackz.release();
		}
	}

	class AudioTrackZThread extends Thread {
		private short m_iAmp = Short.MAX_VALUE;
		private short m_bitDateZ[] = new short[44100];
		private double x = 2.0 * Math.PI * 8250.0 / 44100.0;
		private boolean isRunning = false;

		@Override
		public void run() {
			isRunning = true;
			for (int i = 0; i < 44100; i++) {
				m_bitDateZ[i] = (short) (m_iAmp * Math.sin(x * i));
			}
			audioTrackz.play();
			int m_bitDateZSize = m_bitDateZ.length;
			do {
				audioTrackz.write(m_bitDateZ, 0, m_bitDateZSize);
			} while (isRunning);
			super.run();
		}
	}
}
