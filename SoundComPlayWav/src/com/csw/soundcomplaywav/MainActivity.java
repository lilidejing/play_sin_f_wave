package com.csw.soundcomplaywav;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button playBtn, stopBtn;

	AudioTrack mTrack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		playBtn = (Button) this.findViewById(R.id.playBtn);
		stopBtn = (Button) this.findViewById(R.id.stopBtn);
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}

		});

		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mTrack != null) {
					mTrack.stop();
					mTrack.release();
				}
			}

		});
	}
	short [] shortBuffer;
	Runnable playRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			FileInputStream mFileInputStream = null;
			byte mBuffer[] = null;
			short mLen = 0;

			

			String WAV_PATH = android.os.Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/dictionary";
			System.out.println("目录：" + WAV_PATH);
			String Filename = WAV_PATH + "/" + "shake.wav";
			File file = new File(WAV_PATH);

			try {
				if (!file.exists())
					file.mkdir();
				if (!(new File(Filename)).exists()) {
					InputStream mInputStream = getResources().openRawResource(
							R.raw.shake);
					// 创建输出流
					FileOutputStream fos = new FileOutputStream(Filename);
					// 将数据输出
					byte[] buffer = new byte[10240];
					int count = 0;
					while ((count = mInputStream.read(buffer)) > 0) {
						fos.write(buffer, 0, count);
					}
					// 关闭资源
					fos.close();
					mInputStream.close();

				}

				mFileInputStream = new FileInputStream(Filename);
				
				mBuffer = new byte[10 * 1024];
				
				
				mLen = (short) mFileInputStream.read(mBuffer);
				shortBuffer=new short[10*1024];
				for(int i=0;i<mBuffer.length;i++){
//					System.out.print(str2HexByte(mBuffer));
//					System.out.print( "0x"+Integer.toHexString(mBuffer[i] & 0xFF)+",");
					if(i%2==0){
						shortBuffer[i]=getShort(mBuffer,i);
						System.out.print (" "+shortBuffer[i]);
					}
					
				}
				
//				short shortBuffer1 =getShort(mBuffer ,0);
				System.out.println("");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

//			if (mTrack == null && mFileInputStream != null) {
				try {
					Log.i("Tom", "file lenth is " + mLen);
					mTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
							AudioFormat.CHANNEL_OUT_MONO,
							AudioFormat.ENCODING_PCM_16BIT, mLen,
							AudioTrack.MODE_STATIC);
					mTrack.setStereoVolume(1.0f, 0.0f);
				} catch (Exception e) {
					Log.e("Tom", "can't crate an AudioTrack");
				}
//			}

			if (mTrack != null) {
				short written = (short) mTrack.write(shortBuffer, 0, shortBuffer.length);
				Log.i("Tom", "AudioTrack write " + written);
				mTrack.play();
				Log.i("Tom", "AudioTrack play");
			}
		}

	};

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				new Thread(playRunnable).start();
				break;

			default:

				break;

			}

		};
	};
	
	/**
	* 通过byte数组取到short
	*
	* @param b
	* @param index  第几位开始取
	* @return
	*/
	public static short getShort(byte[] b, int index) {
	      return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
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
