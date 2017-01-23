package com.example.scanmanager;

import com.example.mybutton.MySlipSwitch;

import java.util.zip.Inflater;

import com.example.scanmanager.R;
import com.example.mybutton.MySlipSwitch.OnSwitchListener;
import com.example.scanmanager.MainActivity;

import android.hardware.utils.Scan;
import android.os.Bundle;
import android.provider.Settings;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public final static String LOG_TAG = "Scan Service";
	
	private CheckBox cb_code39;
	private CheckBox cb_code128;
	private CheckBox cb_interleaved_2of5;
	private CheckBox cb_gs1128;
	private CheckBox cb_ean13;
	private CheckBox cb_qrcode;
	private CheckBox cb_pdf417;

	//自定义滑动开关控件
	private MySlipSwitch code39_slipswitch;
	private MySlipSwitch code128_slipswitch;
	private MySlipSwitch interleaved_2of5_slipswitch;
	private MySlipSwitch gs1_128_slipswitch;
	private MySlipSwitch ean_13_slipswitch;

	//条码类型的使能状态
	private static boolean code39_state = true;
	private static boolean code128_state = true;
	private static boolean gs1128_state = true;
	private static boolean ean13_state = true;
	private static boolean interleaved_2of5_state = true;
	private static boolean qr_code_state = true;
	private static boolean pdf417_state = true;

	//显示扫描结果
	private TextView tv_result = null;
	//扫描结果的广播
	private final static String SCAN_RESULT_ACTION = "com.barcode.sendBroadcast";
	private ScanResultRecevier mScanResultRecevier;
	//串口操作本地方法
	public Scan mScan;
	public int enable = 0;
	//存储条码类型状态
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	static {
		System.loadLibrary("n431xutils");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar=getActionBar();
		actionBar.setCustomView(R.layout.actionbar_layout);
		View actionBarLayout=actionBar.getCustomView();
		Button setting=(Button) actionBarLayout.findViewById(R.id.setting_actionbar);
		Log.i("TAG","actionAbarLayout"+actionBarLayout);
		Log.i("TAG","setting"+setting);

		//mScan.openPort("/dev/ttyMT1", 9600);
		ImageButton code39_Edit=(ImageButton) findViewById(R.id.code39_Edit);
		/*        code39_Edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent code39_intent =new Intent(MainActivity.this,Code39Setting.class);
				startActivity(code39_intent);

			}
		});
		 */
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		//注册条码扫描结果接手广播
		mScanResultRecevier = new ScanResultRecevier();
		IntentFilter intentFilter = new IntentFilter(SCAN_RESULT_ACTION);
		this.registerReceiver(mScanResultRecevier, intentFilter);

		//创建ShardPreference存储条码类型状态信息
		sp = getSharedPreferences("bar_type_state", Activity.MODE_PRIVATE);
		editor = sp.edit();

		//获取之前的条码类型状态信息
		code39_state = sp.getBoolean("code39", true);
		code128_state = sp.getBoolean("code128", true);
		gs1128_state = sp.getBoolean("gs1128", true);
		ean13_state = sp.getBoolean("ean13", true);
		interleaved_2of5_state = sp.getBoolean("interleaved_2of5", true);
		qr_code_state = sp.getBoolean("qr_code", true);
		pdf417_state = sp.getBoolean("pdf417", true);

		//复位honyware扫描头至原始状态
		//mScan.dohonywareset();

		tv_result = (TextView) findViewById(R.id.tv_result);

		//自动义滑动控件初始化
		cb_code39 = (CheckBox)findViewById(R.id.code39_checkBox);
		//  code39_slipswitch.setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
		//    code39_slipswitch.setSwitchState(code39_state);
		cb_code39.setChecked(code39_state);
		//根据状态信息使能、禁止该类型条码
		mScan.honywareTypeOnOff("Code 39", code39_state?1:0);

		cb_code39.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Toast.makeText(MainActivity.this, "Code 39 On", 0).show();
					code39_state = true;
				}
				else{
					Toast.makeText(MainActivity.this, "Code 39 Off", 0).show();
					code39_state = false;
				}
				//保存状态信息
				editor.putBoolean("code39", code39_state);
				editor.commit();

				mScan.honywareTypeOnOff("Code 39", code39_state?1:0);
			}
		});

		/*code39_slipswitch.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				// TODO Auto-generated method stub
				if(isSwitchOn) {
					Toast.makeText(MainActivity.this, "Code 39 On", 0).show();
					code39_state = true;
				} else {
					Toast.makeText(MainActivity.this, "Code 39 Off", 0).show();
					code39_state = false;
				}
				//保存状态信息
				editor.putBoolean("code39", code39_state);
				editor.commit();

				mScan.honywareTypeOnOff("Code 39", isSwitchOn?1:0);
			}
		});*/
		cb_code128 = (CheckBox)findViewById(R.id.code128_checkBox);
		//      code128_slipswitch.setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
		//       code128_slipswitch.setSwitchState(code128_state);
		cb_code128.setChecked(code128_state);
		mScan.honywareTypeOnOff("Code 128", code128_state?1:0);
		cb_code128.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub

				if(isChecked){
					Toast.makeText(MainActivity.this, "Code 128 On", 0).show();
					code128_state = true;
				}
				else{
					Toast.makeText(MainActivity.this, "Code 128 Off", 0).show();
					code128_state = false;
				}
				editor.putBoolean("code128", code128_state);
				editor.commit();
				
				mScan.honywareTypeOnOff("Code 128", code128_state?1:0);
			}
		});
		/*	
        code128_checkBox.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				// TODO Auto-generated method stub
				if(isSwitchOn) {
					Toast.makeText(MainActivity.this, "Code 128 On", 0).show();
					code128_state = true;
				} else {
					Toast.makeText(MainActivity.this, "Code 128 Off", 0).show();
					code128_state = false;
				}
				editor.putBoolean("code128", code128_state);
				editor.commit();

				mScan.honywareTypeOnOff("Code 128", isSwitchOn?1:0);
			}
		});*/
		cb_interleaved_2of5 = (CheckBox)findViewById(R.id.interleaved_2of5_checkBox);
		cb_interleaved_2of5.setChecked(interleaved_2of5_state);
		/*
       interleaved_2of5_slipswitch.setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
        interleaved_2of5_slipswitch.setSwitchState(interleaved_2of5_state);*/

		mScan.honywareTypeOnOff("Interleaved 2 of 5", interleaved_2of5_state?1:0);
		cb_interleaved_2of5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Toast.makeText(MainActivity.this, "Interleaved 2 of 5 On", 0).show();
					interleaved_2of5_state = true;
				}
				else{
					Toast.makeText(MainActivity.this, "Interleaved 2 of 5 Off", 0).show();
					interleaved_2of5_state = false;
				}
				editor.putBoolean("interleaved_2of5", interleaved_2of5_state);
				editor.commit();
				
				mScan.honywareTypeOnOff("Interleaved 2 of 5", interleaved_2of5_state?1:0);
			}
		});
		/*  interleaved_2of5_slipswitch.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				// TODO Auto-generated method stub
				if(isSwitchOn) {
					Toast.makeText(MainActivity.this, "Interleaved 2 of 5 On", 0).show();
					interleaved_2of5_state = true;
				} else {
					Toast.makeText(MainActivity.this, "Interleaved 2 of 5 Off", 0).show();
					interleaved_2of5_state = false;
				}
				editor.putBoolean("interleaved_2of5", interleaved_2of5_state);
				editor.commit();

				mScan.honywareTypeOnOff("Interleaved 2 of 5", isSwitchOn?1:0);
			}
		});*/
		cb_gs1128 = (CheckBox)findViewById(R.id.gs1_128_checkBox);
		cb_gs1128.setChecked(gs1128_state);
		/*
        gs1_128_slipswitch.setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
        gs1_128_slipswitch.setSwitchState(gs1128_state);
		 */
		mScan.honywareTypeOnOff("GS1-128", gs1128_state?1:0);
		cb_gs1128.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Toast.makeText(MainActivity.this, "GS1-128 On", 0).show();
					gs1128_state = true;
				}
				else{
					Toast.makeText(MainActivity.this, "GS1-128 Off", 0).show();
					gs1128_state = false;
				}
				editor.putBoolean("gs1128", gs1128_state);
				editor.commit();
				
				mScan.honywareTypeOnOff("GS1-128", gs1128_state?1:0);
			}
		});
		/*
        gs1_128_slipswitch.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				// TODO Auto-generated method stub
				if(isSwitchOn) {
					Toast.makeText(MainActivity.this, "GS1-128 On", 0).show();
					gs1128_state = true;
				} else {
					Toast.makeText(MainActivity.this, "GS1-128 Off", 0).show();
					gs1128_state = false;
				}
				editor.putBoolean("gs1128", gs1128_state);
				editor.commit();

				mScan.honywareTypeOnOff("GS1-128", isSwitchOn?1:0);
			}
		});*/
		cb_ean13 = (CheckBox)findViewById(R.id.ean_13_checkBox);
		cb_ean13.setChecked(ean13_state);
		/*
        ean_13_slipswitch.setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
        ean_13_slipswitch.setSwitchState(ean13_state);
		 */
		mScan.honywareTypeOnOff("EAN-13", ean13_state?1:0);
		cb_ean13.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Toast.makeText(MainActivity.this, "EAN-13 On", 0).show();
					ean13_state = true;
				}
				else{
					Toast.makeText(MainActivity.this, "EAN-13 Off", 0).show();
					ean13_state = false;
				}
				editor.putBoolean("ean13", ean13_state);
				editor.commit();
				
				mScan.honywareTypeOnOff("EAN-13", ean13_state?1:0);
			}
		});
		/* ean_13_slipswitch.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				// TODO Auto-generated method stub
				if(isSwitchOn) {
					Toast.makeText(MainActivity.this, "EAN-13 On", 0).show();
					ean13_state = true;
				} else {
					Toast.makeText(MainActivity.this, "EAN-13 Off", 0).show();
					ean13_state = false;
				}
				editor.putBoolean("ean13", ean13_state);
				editor.commit();

				mScan.honywareTypeOnOff("EAN-13", isSwitchOn?1:0);
			}
		});*/
		
		cb_qrcode = (CheckBox)findViewById(R.id.qr_checkBox);
		cb_qrcode.setChecked(qr_code_state);
		/*
        ean_13_slipswitch.setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
        ean_13_slipswitch.setSwitchState(ean13_state);
		 */
		mScan.honywareTypeOnOff("QR Code", qr_code_state?1:0);
		cb_qrcode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Toast.makeText(MainActivity.this, "QR Code On", 0).show();
					qr_code_state = true;
				}
				else{
					Toast.makeText(MainActivity.this, "QR Code Off", 0).show();
					qr_code_state = false;
				}
				editor.putBoolean("qr_code", qr_code_state);
				editor.commit();
				
				mScan.honywareTypeOnOff("QR Code", qr_code_state?1:0);
			}
		});
		
		cb_pdf417 = (CheckBox)findViewById(R.id.pdf_417_checkBox);
		cb_pdf417.setChecked(pdf417_state);
		/*
        ean_13_slipswitch.setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
        ean_13_slipswitch.setSwitchState(ean13_state);
		 */
		mScan.honywareTypeOnOff("PDF417", pdf417_state?1:0);
		cb_pdf417.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Toast.makeText(MainActivity.this, "PDF417 On", 0).show();
					pdf417_state = true;
				}
				else{
					Toast.makeText(MainActivity.this, "PDF417 Off", 0).show();
					pdf417_state = false;
				}
				editor.putBoolean("pdf417", pdf417_state);
				editor.commit();
				
				mScan.honywareTypeOnOff("PDF417", pdf417_state?1:0);
			}
		});
	}

	//广播接收
	class ScanResultRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if(arg1.getAction().equals(SCAN_RESULT_ACTION)) {
				//显示扫描结果
				String result = arg1.getStringExtra("BARCODE");
				if(SettingsActivity.getRg_separator_state() == 2) {
					result += " ";
				}
				tv_result.setText("Scan Result:"+result);
				Toast.makeText(getApplicationContext(), result, 0).show();
			}else {
				Toast.makeText(getApplication(), "scan timeout", 0).show();
				tv_result.setText("Scan Result:");
			}
		}

	}
	public void doClick(View view){
		switch (view.getId()) {
		case R.id.code39_Edit:
			Intent code39_intent =new Intent(MainActivity.this,Code39Setting.class);
			startActivity(code39_intent);
			break;
		case R.id.code128_Edit:
			Intent code128_intent =new Intent(MainActivity.this,Code128Setting.class);
			startActivity(code128_intent);
			break;
		case R.id.interleaved_2of5_Edit:
			Intent interLeaved2af5_intent =new Intent(MainActivity.this,Interleaved2af5Setting.class);
			startActivity(interLeaved2af5_intent);
			break;
		case R.id.gs1_128_Edit:
			Intent gsi_128_intent =new Intent(MainActivity.this,Gsi128Setting.class);
			startActivity(gsi_128_intent);
			break;
		case R.id.ean_13_Edit:
			Intent ean_13_intent =new Intent(MainActivity.this,EAN13Setting.class);
			startActivity(ean_13_intent);
			break;
		case R.id.qr_Edit:
			Intent qr_intent =new Intent(MainActivity.this,QRCodeSetting.class);
			startActivity(qr_intent);
			break;
		case R.id.pdf_417_Edit:
			Intent pdf_417_intent =new Intent(MainActivity.this,PDF417Setting.class);
			startActivity(pdf_417_intent);
			break;
			default:
				break;
		}

	}
	

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		//mScan.closePort();
		return super.isDestroyed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub



    	switch (item.getItemId()) {  
        case R.id.setting_actionbar:  
          //Intent intent = new Intent(this, NavigationActivity.class);  
          Log.i("TAG","已经才是进入选择区域");
          //startActivity(intent); 
          Toast.makeText(this, "setting++++", 0).show();
          break;
    	case R.id.action_settings:
    		Intent intent = new Intent(this, SettingsActivity.class);
    		startActivity(intent); 
    		break;
        }

    	return super.onOptionsItemSelected(item);
    }

}
