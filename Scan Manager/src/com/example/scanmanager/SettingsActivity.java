package com.example.scanmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SettingsActivity extends Activity {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	private int rg_separator_id = 0;
	private int rg_receive_id = 0;
	
	private static int rg_separator_state = 0;
	private static int rg_receive_state = 0;
	

	public static int getRg_separator_state() {
		return rg_separator_state;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_setting);
		
		sp = getSharedPreferences("scan_settings", Activity.MODE_PRIVATE);
		editor = sp.edit();
		
		rg_separator_id = sp.getInt("rg_separator_check", R.id.rg_scan_separator_no_newline);
		rg_receive_id = sp.getInt("rg_receive_check", R.id.rg_scan_receive_models_fast);
		
		RadioGroup rg_receive=(RadioGroup) findViewById(R.id.rg_scan_receive_models);
		rg_receive.check(rg_receive_id);
		rg_receive.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rg_scan_receive_models_fast:
					rg_receive_state = 0;
					break;
				case R.id.rg_scan_receive_models_slow:
					rg_receive_state = 1;
					break;
				case R.id.rg_scan_receive_models_broadcast:
					rg_receive_state = 2;
					break;
				default:
					break;
				}
				editor.putInt("rg_receive_check", checkedId);
				editor.commit();
				
				Settings.System.putInt(getContentResolver(), "barcode_receive_models_settings", rg_receive_state);
			}
		});
		
		RadioGroup rg_separator=(RadioGroup) findViewById(R.id.rg_scan_separator_models);
		rg_separator.check(rg_separator_id);
		rg_separator.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rg_scan_separator_no_newline:
					rg_separator_state = 0;
					break;
				case R.id.rg_scan_separator_newline:
					rg_separator_state = 1;
					break;
				case R.id.rg_scan_separator_space:
					rg_separator_state = 2;
					break;
				default:
					break;
				}
				editor.putInt("rg_separator_check", checkedId);
				editor.commit();
				
				if(rg_separator_state != 2)
					Settings.System.putInt(getContentResolver(), "barcode_separator_settings", rg_separator_state);
			}
		});
	}

}
