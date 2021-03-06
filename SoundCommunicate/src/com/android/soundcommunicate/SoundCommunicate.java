package com.android.soundcommunicate;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SoundCommunicate extends Activity {
	public final String TAG = "SoundCommunicate";
	public final static int maxDataBufSize = 256;
	public final static int POWERSUPPLY = 0;
	public final static int MESSAGEOUT = 1;
	private final int minFrequency = 100;
	private final int powerSupplyFreq = 3400;
	private int oldFrequency = 0;
	private int latestFrequency = powerSupplyFreq;
	private boolean powerOnOff = false;
	MessageOut msg;
	PowerSupply power;
	EncoderCore cc;
	SoundRecord myRec; 
	ToggleButton powerOnOffTB;
	ToggleButton recordTB;
	Button sendMsgBT;
	SeekBar freqSB;
	TextView freqTV;
	TextView recMsgTV;
	EditText msgSendET;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound_communicate);
		powerOnOffTB = (ToggleButton) findViewById(R.id.audioPlayButton);
		sendMsgBT = (Button) findViewById(R.id.sendMsgBtuuon);
		msgSendET = (EditText) findViewById(R.id.et_MsgSend);
		recordTB = (ToggleButton) findViewById(R.id.recordButton);
		freqSB = (SeekBar) findViewById(R.id.freqSeekBar);
		freqTV = (TextView) findViewById(R.id.freqTV);
		recMsgTV = (TextView)findViewById(R.id.tv_recMsg);
		cc = new EncoderCore(); 
		power = new PowerSupply();
		msg = new MessageOut(); 
		myRec = new SoundRecord();
		
		freqTV.setText(SoundCommunicate.this.getString(R.string.powerFreq) + String.valueOf(latestFrequency) + "Hz");
		
		powerOnOffButtonProcess();
		
		sendMsgButtionProcess();
		
		recordButtonProcess();
	}
	
	private void recordButtonProcess(){
		recordTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					myRec.start();
					recMsgTV.setText(SoundCommunicate.this.getString(R.string.havdRecdMsgs) + "Message test!");
				}
				else{
					myRec.stop();
					recMsgTV.setText(SoundCommunicate.this.getString(R.string.havdRecdMsgs));
				}
			}
		});
	}


	private void powerOnOffButtonProcess(){
		powerOnOffTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (isChecked) {			
					powerOnOff = true;
					// 左声道提供电源支持
					power.pwStart(cc.carrierSignalGen(latestFrequency));
					freqSeekBarProcess();
				} else { 
					power.pwStop();
					powerOnOff = false;
				}
			}
		});
	}

	private void sendMsgButtionProcess(){
		sendMsgBT.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				byte[] bts = cc.soundCording(msgSendET.getText().toString().getBytes());
				
				if(bts!= null)
					msg.msgStart(bts);		
			}
		});
	}
	private void freqSeekBarProcess(){
		freqSB.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			public void onProgressChanged(SeekBar seekBar,int progress, boolean fromUser) {
				if (progress < minFrequency) {
					seekBar.setProgress(minFrequency);
					return;
				}
				
				latestFrequency = progress;
				if (oldFrequency != latestFrequency) {
					oldFrequency = latestFrequency;
					freqTV.setText(SoundCommunicate.this.getString(R.string.powerFreq) + String.valueOf(latestFrequency) + "Hz");

					if (powerOnOff) {
						power.pwStart(cc.carrierSignalGen(latestFrequency));
					}
				} 
		}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(power != null){
			power.pwStop();
			power = null;
		}
		if (msg != null) {
			msg.msgStop();
			msg = null;
		}
		//android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_sound_communicate, menu);
		return true;
	}
}
