package com.example.scanmanager;

import com.example.scanmanager.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.hardware.utils.CodeParms;
import android.hardware.utils.Scan;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class EAN13Setting extends Activity {

	CodeParms cp_ean13;
	Scan mScan;
	
	private String str_pre = "";
	private String str_suf = "";
	private TextView tv_ean13_pre;
	private TextView tv_ean13_suf;
	
	private CheckBox cb_prefix;
	private CheckBox cb_suffix;
	private CheckBox cb_convert_upc_to_ean;
	
	private boolean convert_upc_to_ean = false;
	private boolean cb_pre_state = false;
	private boolean cb_suf_state = false;
	private boolean pre_state = false;
	private boolean suf_state = false;
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	private void codeParmsinit() {
		cp_ean13 = new CodeParms(0x05);
		cp_ean13.Flags = 0x00;
		cp_ean13.MinLength = 0;
		cp_ean13.MaxLength = 0;
		cp_ean13.Prefix = 0x00;
		cp_ean13.Suffix = 0x00;
		cp_ean13.StrPrefix = "";
		cp_ean13.StrSuffix = "";
		cp_ean13.upcToEan = 0x00;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ean13_setting);
		
		codeParmsinit();
		
		sp = getSharedPreferences("ean13_config", Activity.MODE_PRIVATE);
		editor = sp.edit();
		
		convert_upc_to_ean = sp.getBoolean("convert_upc_to_ean", false);
		pre_state = sp.getBoolean("prefix", false);
		suf_state = sp.getBoolean("suffix", false);
		str_pre = sp.getString("str_pre", "");
		str_suf = sp.getString("str_suf", "");
		
		cp_ean13.upcToEan = convert_upc_to_ean ? 0x01 : 0x00;
		cp_ean13.Prefix = pre_state ? 0x01 : 0x00;
		cp_ean13.Suffix = suf_state ? 0x01 : 0x00;
		cp_ean13.StrPrefix = str_pre;
		cp_ean13.StrSuffix = str_suf;
		
		mScan.setSymbologyConfig(cp_ean13);
		
		ActionBar actionBar=getActionBar();
		actionBar.setCustomView(R.layout.actionbar_layout);
		View actionBarLayout=actionBar.getCustomView();
		TextView settingTitle=(TextView) actionBarLayout.findViewById(R.id.title_actionBar);
		settingTitle.setText(getResources().getString(R.string.ean_13));
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		
		cb_prefix = (CheckBox) findViewById(R.id.cb_ean13_pre);
		cb_suffix = (CheckBox) findViewById(R.id.cb_ean13_suf);
		cb_convert_upc_to_ean = (CheckBox) findViewById(R.id.Convert_checkBox);
		tv_ean13_pre = (TextView) findViewById(R.id.tv_ean13_pre);
		tv_ean13_suf = (TextView) findViewById(R.id.tv_ean13_suf);
		
		cb_convert_upc_to_ean.setChecked(convert_upc_to_ean);
		cb_prefix.setChecked(pre_state);
		cb_suffix.setChecked(suf_state);
		tv_ean13_pre.setText(str_pre);
		tv_ean13_suf.setText(str_suf);
		cb_convert_upc_to_ean.setChecked(convert_upc_to_ean);
		

		cb_pre_state = pre_state;
		cb_suf_state = suf_state;
		
		cb_prefix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cp_ean13.Prefix = 0x01;
					cb_pre_state = true;
				}else {
					cp_ean13.Prefix = 0x00;
					cb_pre_state = false;
					cp_ean13.StrPrefix = "";
					str_pre = "";
					editor.putString("str_pre", str_pre);
					tv_ean13_pre.setText(str_pre);
				}
				editor.putBoolean("prefix", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_ean13);
			}
		});
		
		cb_suffix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cp_ean13.Suffix = 0x01;
					cb_suf_state = true;
				}else {
					cp_ean13.Suffix = 0x00;
					cb_suf_state = false;
					cp_ean13.StrSuffix = "";
					str_suf = "";
					editor.putString("str_suf", str_suf);
					tv_ean13_suf.setText(str_suf);
				}
				editor.putBoolean("suffix", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_ean13);
			}
		});
		
		cb_convert_upc_to_ean.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					cp_ean13.upcToEan = 0x01;
				}
				else{
					cp_ean13.upcToEan = 0x00;
				}

				editor.putBoolean("convert_upc_to_ean", isChecked);
				editor.commit();

				mScan.setSymbologyConfig(cp_ean13);
			}
		});
	}


	public void doClick(View view){
		switch (view.getId()) {

		case R.id.et_ean13_prefix:
			//minimum's edit
			if(cb_pre_state) {
			final EditText edittext3=new EditText(this);
	new AlertDialog.Builder(this).setTitle("please set the prefix that you want to edit").setView(edittext3).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					str_pre = edittext3.getText().toString();
					cp_ean13.StrPrefix = str_pre;
					tv_ean13_pre.setText(str_pre);
					
					editor.putString("str_pre", str_pre);
					editor.commit();
					mScan.setSymbologyConfig(cp_ean13);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			}else {
				Toast.makeText(getApplicationContext(), "please select the Prefix CheckBox", 0).show();
			}
			break;
		case R.id.et_ean13_suffix:
			//minimum's edit
			if(cb_suf_state) {
			final EditText edittext4=new EditText(this);
	new AlertDialog.Builder(this).setTitle("please set the suffix that you want to edit").setView(edittext4).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					str_suf = edittext4.getText().toString();
					cp_ean13.StrSuffix = str_suf;
					tv_ean13_suf.setText(str_suf);
					
					editor.putString("str_suf", str_suf);
					editor.commit();
					mScan.setSymbologyConfig(cp_ean13);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			}else{
				Toast.makeText(getApplicationContext(), "please select the Suffix CheckBox", 0).show();
			}
			break;
		case R.id.et_ean13_prefix_delete:
			//minimum's edit
	new AlertDialog.Builder(this).setTitle("Delete the prefix ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cp_ean13.Prefix = 0x00;
					cp_ean13.StrPrefix = "";
					mScan.setSymbologyConfig(cp_ean13);
					
					pre_state = false;
					str_pre = "";
					editor.putString("str_pre", str_pre);
					editor.putBoolean("prefix", pre_state);
					editor.commit();
					cb_prefix.setChecked(pre_state);
					tv_ean13_pre.setText(str_pre);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			break;
		case R.id.et_ean13_suffix_delete:
			//minimum's edit
	new AlertDialog.Builder(this).setTitle("Delete the suffix ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cp_ean13.Suffix = 0x00;
					cp_ean13.StrSuffix = "";
					mScan.setSymbologyConfig(cp_ean13);

					suf_state = false;
					str_suf = "";
					editor.putString("str_suf", str_suf);
					editor.putBoolean("suffix", suf_state);
					editor.commit();
					cb_suffix.setChecked(suf_state);
					tv_ean13_suf.setText(str_suf);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			break;


		default:
			break;
		}

	}
}
