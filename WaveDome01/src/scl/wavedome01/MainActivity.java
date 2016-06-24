package scl.wavedome01;

import scl.waveout.WaveOutF;
import scl.waveout.WaveOutZ;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btnPlay1KHz;
	private Button btnStop;
	private Button btnPlay2KHz;
	private Button btnPlay4KHz;
	private WaveOutF woF;
	private WaveOutZ woZ;
	private byte[] byteDate;
	private int len;
	private byte[] byteDate1;
	private int len1;
	private int i = 0;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		byteDate = new byte[] { (byte) 0x55};
		len = byteDate.length;

		byteDate1 = new byte[] {(byte) 0x55};
		len1 = byteDate1.length;

	}

	private void init() {
		btnPlay1KHz = (Button) findViewById(R.id.btn_play1Khz);
		btnPlay2KHz = (Button) findViewById(R.id.btn_play2Khz);
		btnPlay4KHz=(Button)findViewById(R.id.btn_play4Khz);

		btnStop = (Button) findViewById(R.id.btn_stop);

		btnPlay1KHz.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (woF != null) {
					woF = null;
				}
				
				if (woF == null) {
					woF = new WaveOutF(44);
				}
				
				if (i % 2 == 0) {
					i++;
					woF.sendByteDate(byteDate, len,44);
				} else {
					i++;
					woF.sendByteDate(byteDate1, len1,44);
				}
			}
		});
		btnPlay2KHz.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (woF != null) {
					woF = null;
				}
				
				if (woF == null) {
					woF = new WaveOutF(22);
				}
				
				if (i % 2 == 0) {
					i++;
					woF.sendByteDate(byteDate, len,22);
				} else {
					i++;
					woF.sendByteDate(byteDate1, len1,22);
				}
			}
		});
		btnPlay4KHz.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (woF != null) {
					woF = null;
				}
				
				if (woF == null) {
					woF = new WaveOutF(11);
				}
				
				if (i % 2 == 0) {
					i++;
					woF.sendByteDate(byteDate, len,11);
				} else {
					i++;
					woF.sendByteDate(byteDate1, len1,11);
				}
			}
		});

		

		btnStop.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(woF!=null){
					WaveOutF.isRunning=false;
					woF.closeAudioTrack();
				}
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(woF!=null){
				WaveOutF.isRunning=false;
				woF.closeAudioTrack();
			}
//			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
