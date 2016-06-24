package com.csw.soundcom;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private static String TAG = "MainActivity";
	private Button firstBtn, secondBtn, thirdBtn, fourthBtn;

	/**
	 * 5个4KHz的正弦波buffer
	 */
	private byte[] byteBuffer_4KHz_5;

	/**
	 * 55AA的正弦波buffer
	 */
	private byte[] byteBuffer_55AA;
	/**
	 * 单个1KHz正弦波buffer
	 */
	private byte[] singleSinByteBuffer_1KHz;
	/**
	 * 单个2KHz正弦波buffer
	 */
	private byte[] singleSinByteBuffer_2KHz;
	/**
	 * 采样率
	 */
	private static final int SAMPLING_RATE = 44100;
	/**
	 * 1Khz
	 */
	private static final int Hz_1000 = 1000;
	/**
	 * 2Khz
	 */
	private static final int Hz_2000 = 2000;
	/**
	 * 4KHZ
	 */
	private static final int Hz_4000 = 4000;

	/**
	 * 要发送出去的buffer
	 */
	private byte[] sendByteBuffer;
/**
 * 获取对应音频采样率下的最小缓冲区的大小
 */
	private int  minBuffSize = AudioTrack.getMinBufferSize(SAMPLING_RATE, AudioFormat.CHANNEL_CONFIGURATION_STEREO,  AudioFormat.ENCODING_PCM_8BIT);
	
	
	private AudioTrack audioTrack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initWeight();
		mHandler.sendEmptyMessage(0);

		System.out.println("minBuffSize="+minBuffSize);
	}

	/**
	 * 初始化buffer数据
	 */
	private void initBuffer() {
		byteBuffer_4KHz_5 = getFiveSinBuffer();

		singleSinByteBuffer_1KHz = getSingleBuffer_1KHz();
		singleSinByteBuffer_2KHz = getSingleBuffer_2KHz();
		byteBuffer_55AA = get55AAsinBuffer();
		sendByteBuffer = getAppendBuffer(byteBuffer_4KHz_5, byteBuffer_55AA);

		firstBtn.setClickable(true);
	}

	/**
	 * 初始化控件
	 */
	private void initWeight() {
		firstBtn = (Button) this.findViewById(R.id.firstModelBtn);
		secondBtn = (Button) this.findViewById(R.id.secondModelBtn);
		thirdBtn = (Button) this.findViewById(R.id.thirdModelBtn);
		fourthBtn = (Button) this.findViewById(R.id.fourthModelBtn);
		firstBtn.setOnClickListener(this);
		secondBtn.setOnClickListener(this);
		thirdBtn.setOnClickListener(this);
		fourthBtn.setOnClickListener(this);
		firstBtn.setClickable(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.firstModelBtn:
			mHandler.sendEmptyMessage(1);
			break;
		case R.id.secondModelBtn:

			break;
		case R.id.thirdModelBtn:

			break;
		case R.id.fourthModelBtn:

			break;
		default:

			break;

		}
	}
	
	Runnable initBufferRunnable=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			initBuffer();
			
		}
		
	};
	
	
	Runnable modelOneRunnable = new Runnable() {
		public void run() {

			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					SAMPLING_RATE,
					AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_8BIT, minBuffSize,
					AudioTrack.MODE_STREAM);
			if (audioTrack != null) {
				audioTrack.setStereoVolume(1, 0);
				audioTrack.play();
//				while(true){
					/*try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					audioTrack.write(sendByteBuffer, 0, sendByteBuffer.length);
					audioTrack.flush();
//				}
			}
			
			
			
		};
	};

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
                new Thread(initBufferRunnable).start();
				break;
				
			case 1:
				new Thread(modelOneRunnable).start();
				break;
			default:
				break;

			}
		}
	};

	/**
	 * 拼接buffer
	 * 
	 * @author lgj
	 * @param byteBuffer1
	 *            ,byteBuffer2要拼接的buffer
	 * @return byteBufferTemp,合成后的buffer
	 */

	public byte[] getAppendBuffer(byte[] byteBuffer1, byte[] byteBuffer2) {
		int byteBuffer1_length = byteBuffer1.length;
		int byteBuffer2_length = byteBuffer2.length;
		System.out.println("byteBuffer1_length=" + byteBuffer1_length);
		System.out.println("byteBuffer2_length=" + byteBuffer2_length);
		byte[] byteBufferTemp = new byte[byteBuffer1_length
				+ byteBuffer2_length];

		System.arraycopy(byteBuffer1, 0, byteBufferTemp, 0, byteBuffer1_length);
		System.arraycopy(byteBuffer2, 0, byteBufferTemp, byteBuffer1_length,
				byteBuffer2_length);

		return byteBufferTemp;

	}

	/**
	 * @author lgj
	 * @param buffer
	 *            ，正弦波buffer，开始为空
	 * @param singleBufferLength
	 *            ,单个正弦波波长
	 * @param length
	 *            ，正弦波长
	 * @return sinByteBuffer，正弦波buffer
	 */
	@SuppressWarnings("unused")
	private byte[] getSinBuffer(byte[] buffer, int singleBufferLength,
			int length) {
		byte[] sinByteBuffer = SinWave.sin(buffer, singleBufferLength, length);

		return sinByteBuffer;
	}

	/**
	 * 获取5个4KHz正弦波buffer
	 * 
	 * @return
	 */
	public byte[] getFiveSinBuffer() {

		int singleBufferLength = SAMPLING_RATE / Hz_4000;// 4Khz单个正弦波波长

		byte[] single4KhzBuffer = new byte[singleBufferLength * 5];
		byte[] five4KhzBufferTemp = getSinBuffer(single4KhzBuffer,
				singleBufferLength, singleBufferLength * 5);

		return five4KhzBufferTemp;
	}

	/**
	 * 得到一个1Khz的正弦波Buffer
	 */
	public byte[] getSingleBuffer_1KHz() {
		int singleBufferLength = SAMPLING_RATE / Hz_1000;// 1Khz单个正弦波波长
		byte[] single1KhzBuffer = new byte[singleBufferLength];
		byte[] single1KhzBufferTemp = getSinBuffer(single1KhzBuffer,
				singleBufferLength, singleBufferLength);

		return single1KhzBufferTemp;
	}

	/**
	 * 得到一个2KHz的正弦波buffer
	 * 
	 * @return
	 */
	public byte[] getSingleBuffer_2KHz() {
		int singleBufferLength = SAMPLING_RATE / Hz_2000;// 1Khz单个正弦波波长
		byte[] single1KhzBuffer = new byte[singleBufferLength];
		byte[] single1KhzBufferTemp = getSinBuffer(single1KhzBuffer,
				singleBufferLength, singleBufferLength);

		return single1KhzBufferTemp;
	}

	/**
	 * 获取55AA正弦波的buffer
	 */

	public byte[] get55AAsinBuffer() {
		byte[] byte55AABufferTemp = new byte[SAMPLING_RATE];
		String binary = hexString2binaryString("55AA");

		Log.d(TAG, "55AA=" + binary);
		char item;
		for (int i = 0; i < binary.length(); i++) {
			if (i == 0) {
				item = binary.charAt(i);

				if (item == '0') {
					byte55AABufferTemp = singleSinByteBuffer_1KHz;
				} else if (item == '1') {
					byte55AABufferTemp = singleSinByteBuffer_2KHz;
				}
				System.out.println("byte55AABufferTemp="
						+ byte55AABufferTemp.length);
				continue;
			}
			item = binary.charAt(i);

			if (item == '0') {
				byte55AABufferTemp = getAppendBuffer(byte55AABufferTemp,
						singleSinByteBuffer_1KHz);
			} else if (item == '1') {
				byte55AABufferTemp = getAppendBuffer(byte55AABufferTemp,
						singleSinByteBuffer_2KHz);
			}
			System.out.println("byte55AABufferTemp="
					+ byte55AABufferTemp.length);
		}
		return byte55AABufferTemp;

	}

	/**
	 * 16进制字符串转成2进制字符串
	 * 
	 * @param hexString
	 * @return
	 */

	public static String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(
							hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	/**
	 * 字节转换成十六进制字符串
	 */
	public static String str2HexByte(byte[] buffer) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = buffer;
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}
}
