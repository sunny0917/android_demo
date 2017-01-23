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
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Gsi128Setting extends Activity {

	CodeParms cp_gs1128;
	Scan mScan;
	
	private int val_mini = 0;
	private int val_maxi = 48;
	private String str_pre = "";
	private String str_suf = "";
	private TextView tv_gs1128_mini;
	private TextView tv_gs1128_maxi;
	private TextView tv_gs1128_pre;
	private TextView tv_gs1128_suf;
	
	private CheckBox cb_prefix;
	private CheckBox cb_suffix;
	private CheckBox cb_miniLen;
	private CheckBox cb_maxiLen;
	
	private boolean cb_mini = false;
	private boolean cb_maxi = false;
	private boolean cb_pre_state = false;
	private boolean cb_suf_state = false;
	private boolean mini_state = false;
	private boolean maxi_state = false;
	private boolean pre_state = false;
	private boolean suf_state = false;
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	private void codeParmsinit() {
		cp_gs1128 = new CodeParms(0x04);
		cp_gs1128.Flags = 0x00;
		cp_gs1128.MinLength = 1;
		cp_gs1128.MaxLength = 80;
		cp_gs1128.Prefix = 0x00;
		cp_gs1128.Suffix = 0x00;
		cp_gs1128.StrPrefix = "";
		cp_gs1128.StrSuffix = "";
		cp_gs1128.upcToEan = 0x00;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gsi128_setting);
		
		codeParmsinit();
		
		sp = getSharedPreferences("gs1128_config", Activity.MODE_PRIVATE);
		editor = sp.edit();
		
		mini_state = sp.getBoolean("mini", false);
		maxi_state = sp.getBoolean("maxi", false);
		pre_state = sp.getBoolean("prefix", false);
		suf_state = sp.getBoolean("suffix", false);
		val_mini = sp.getInt("minilength", 1);
		val_maxi = sp.getInt("maxilength", 80);
		str_pre = sp.getString("str_pre", "");
		str_suf = sp.getString("str_suf", "");
		
		cp_gs1128.MinLength = val_mini;
		cp_gs1128.MaxLength = val_maxi;
		cp_gs1128.Prefix = pre_state ? 0x01 : 0x00;
		cp_gs1128.Suffix = suf_state ? 0x01 : 0x00;
		cp_gs1128.StrPrefix = str_pre;
		cp_gs1128.StrSuffix = str_suf;
		
		mScan.setSymbologyConfig(cp_gs1128);
		
		 ActionBar actionBar=getActionBar();
	     actionBar.setCustomView(R.layout.actionbar_layout);
	        View actionBarLayout=actionBar.getCustomView();
	        TextView settingTitle=(TextView) actionBarLayout.findViewById(R.id.title_actionBar);
	        settingTitle.setText(getResources().getString(R.string.gsi_128));
	        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	        actionBar.setDisplayShowCustomEnabled(true);
	        
        cb_prefix = (CheckBox) findViewById(R.id.cb_gs1128_pre);
		cb_suffix = (CheckBox) findViewById(R.id.cb_gs1128_suf);
		cb_miniLen = (CheckBox) findViewById(R.id.cb_gs1128_mini);
		cb_maxiLen = (CheckBox) findViewById(R.id.cb_gs1128_maxi);
		tv_gs1128_mini = (TextView) findViewById(R.id.tv_gs1128_mini);
		tv_gs1128_maxi = (TextView) findViewById(R.id.tv_gs1128_maxi);
		tv_gs1128_pre = (TextView) findViewById(R.id.tv_gs1128_pre);
		tv_gs1128_suf = (TextView) findViewById(R.id.tv_gs1128_suf);
		
		cb_miniLen.setChecked(mini_state);
		cb_maxiLen.setChecked(maxi_state);
		cb_prefix.setChecked(pre_state);
		cb_suffix.setChecked(suf_state);
		tv_gs1128_mini.setText(val_mini+"");
		tv_gs1128_maxi.setText(val_maxi+"");
		tv_gs1128_pre.setText(str_pre);
		tv_gs1128_suf.setTag(str_suf);
		
		cb_maxi = maxi_state;
		cb_mini = mini_state;
		cb_pre_state = pre_state;
		cb_suf_state = suf_state;
		
		cb_prefix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cp_gs1128.Prefix = 0x01;
					cb_pre_state = true;
				}else {
					cp_gs1128.Prefix = 0x00;
					cb_pre_state = false;
					cp_gs1128.StrPrefix = "";
					str_pre = "";
					editor.putString("str_pre", str_pre);
					tv_gs1128_pre.setText(str_pre);
				}
				editor.putBoolean("prefix", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_gs1128);
			}
		});
		
		cb_suffix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cp_gs1128.Suffix = 0x01;
					cb_suf_state = true;
				}else {
					cp_gs1128.Suffix = 0x00;
					cb_suf_state = false;
					cp_gs1128.StrSuffix = "";
					str_suf = "";
					editor.putString("str_suf", str_suf);
					tv_gs1128_suf.setText(str_suf);
				}
				editor.putBoolean("suffix", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_gs1128);
			}
		});
		
		cb_miniLen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cb_mini = true;
				}else {
					cb_mini = false;
					cp_gs1128.MinLength = 1;
					val_mini = 1;
					editor.putInt("minilength", val_mini);
					tv_gs1128_mini.setText(val_mini+"");
				}
				editor.putBoolean("mini", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_gs1128);
			}
		});
		
		cb_maxiLen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cb_maxi = true;
				}else {
					cb_maxi = false;
					cp_gs1128.MaxLength = 80;
					val_maxi = 80;
					editor.putInt("maxilength", val_maxi);
					tv_gs1128_maxi.setText(val_maxi+"");
				}
				editor.putBoolean("maxi", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_gs1128);
			}
		});
	}
	public void doClick(View view){
		switch (view.getId()) {
		case R.id.et_gs1128_mini:
			//minimum's edit
			if(cb_mini) {
			 final EditText edittext1=new EditText(this);
			 edittext1.setInputType(InputType.TYPE_CLASS_NUMBER);
			 edittext1.setText(val_mini+"");
			 edittext1.setSelection((val_mini+"").length());
			new AlertDialog.Builder(this).setTitle("Please set an integer value (1-80):").setView(edittext1).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					int miniLen = Integer.parseInt(edittext1.getText().toString());
					//Toast.makeText(getApplicationContext(), "The MiniLen is (1~80)"+miniLen, 0).show();
					if((miniLen < 1) || (miniLen > 80)) {
						new AlertDialog.Builder(Gsi128Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub	
							}
						}).create().show();
					}else {
						cp_gs1128.MinLength = miniLen;
						val_mini = miniLen;
						editor.putInt("minilength", val_mini);
						editor.commit();
						tv_gs1128_mini.setText(val_mini+"");
					}
					
					mScan.setSymbologyConfig(cp_gs1128);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			}else {
				Toast.makeText(getApplicationContext(), "please check the MiniLen CheckBox", 0).show();
			}
			break;
		case R.id.et_gs1128_maxi:
			//minimum's edit
			if(cb_maxi) {
			final EditText edittext2=new EditText(this);
			edittext2.setInputType(InputType.TYPE_CLASS_NUMBER);
			 edittext2.setText(val_maxi+"");
			 edittext2.setSelection((val_maxi+"").length());
	new AlertDialog.Builder(this).setTitle("Please set an integer value (1-80):").setView(edittext2).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					int maxiLen = Integer.parseInt(edittext2.getText().toString());
					if((maxiLen < 1) || (maxiLen > 80)) {
						new AlertDialog.Builder(Gsi128Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub		
							}
						}).create().show();
					}else {
						cp_gs1128.MaxLength = maxiLen;
						val_maxi = maxiLen;
						editor.putInt("maxilength", val_maxi);
						editor.commit();
						tv_gs1128_maxi.setText(val_maxi+"");
					}
					
					mScan.setSymbologyConfig(cp_gs1128);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			}else {
				Toast.makeText(getApplicationContext(), "please check the MaxiLen CheckBox", 0).show();
			}
			break;
		case R.id.et_gs1128_mini_delete:
			//minimum's edit
			new AlertDialog.Builder(this).setTitle("Delete the manimum symbol ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cp_gs1128.MinLength = 1;
					val_mini = 1;
					mini_state = false;
					editor.putBoolean("mini_state", mini_state);
					editor.putInt("minilength", val_mini);
					editor.commit();
					tv_gs1128_mini.setText(val_mini+"");
					cb_miniLen.setChecked(mini_state);
					mScan.setSymbologyConfig(cp_gs1128);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			break;
		case R.id.et_gs1128_maxi_delete:
			//minimum's edit
			new AlertDialog.Builder(this).setTitle("Delete the maximum symbol ?").setPositiveButton("OK",new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								cp_gs1128.MaxLength = 80;
								val_maxi = 80;
								maxi_state = false;
								editor.putBoolean("maxi_state", maxi_state);
								editor.putInt("maxilength", val_maxi);
								editor.commit();
								tv_gs1128_maxi.setText(val_maxi+"");
								cb_maxiLen.setChecked(maxi_state);
								mScan.setSymbologyConfig(cp_gs1128);
							}
						}).setNegativeButton("cancel",new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						}).create().show();
			break;

		case R.id.et_gs1128_prefix:
			//minimum's edit
			if(cb_pre_state) {
			final EditText edittext3=new EditText(this);
	new AlertDialog.Builder(this).setTitle("please set the prefix that you want to edit").setView(edittext3).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					str_pre = edittext3.getText().toString();
					cp_gs1128.StrPrefix = str_pre;
					tv_gs1128_pre.setText(str_pre);
					
					editor.putString("str_pre", str_pre);
					editor.commit();
					mScan.setSymbologyConfig(cp_gs1128);
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
		case R.id.et_gs1128_suffix:
			//minimum's edit
			if(cb_suf_state) {
			final EditText edittext4=new EditText(this);
	new AlertDialog.Builder(this).setTitle("please set the suffix that you want to edit").setView(edittext4).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					str_suf= edittext4.getText().toString();
					cp_gs1128.StrSuffix = str_suf;
					tv_gs1128_suf.setText(str_suf);
					
					editor.putString("str_suf", str_suf);
					editor.commit();
					mScan.setSymbologyConfig(cp_gs1128);
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
		case R.id.et_gs1128_prefix_delete:
			//minimum's edit
	new AlertDialog.Builder(this).setTitle("Delete the prefix ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cp_gs1128.Prefix = 0x00;
					cp_gs1128.StrPrefix = "";
					mScan.setSymbologyConfig(cp_gs1128);
					
					pre_state = false;
					str_pre = "";
					editor.putString("str_pre", str_pre);
					editor.putBoolean("prefix", pre_state);
					editor.commit();
					cb_prefix.setChecked(pre_state);
					tv_gs1128_pre.setText(str_pre);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			break;
		case R.id.et_gs1128_suffix_delete:
			//minimum's edit
	new AlertDialog.Builder(this).setTitle("Delete the suffix ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cp_gs1128.Suffix = 0x00;
					cp_gs1128.StrSuffix = "";
					mScan.setSymbologyConfig(cp_gs1128);
					
					suf_state = false;
					str_suf = "";
					editor.putString("str_suf", str_suf);
					editor.putBoolean("suffix", suf_state);
					editor.commit();
					cb_suffix.setChecked(suf_state);
					tv_gs1128_suf.setText(str_suf);
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
