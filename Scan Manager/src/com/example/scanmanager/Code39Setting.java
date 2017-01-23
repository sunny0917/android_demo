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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Code39Setting extends Activity {

	CodeParms cp_code39;
	Scan mScan;
	
	private int val_mini = 0;
	private int val_maxi = 48;
	private String str_pre = "";
	private String str_suf = "";
	private TextView tv_code39_mini;
	private TextView tv_code39_maxi;
	private TextView tv_code39_pre;
	private TextView tv_code39_suf;
	
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
	private int rg_id = 0;
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	private void codeParmsinit() {
		cp_code39 = new CodeParms(0x01);
		cp_code39.Flags = 0x00;
		cp_code39.MinLength = 0;
		cp_code39.MaxLength = 48;
		cp_code39.Prefix = 0x00;
		cp_code39.Suffix = 0x00;
		cp_code39.StrPrefix = "";
		cp_code39.StrSuffix = "";
		cp_code39.upcToEan = 0x00;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_code39_setting);
		
		codeParmsinit();
		
		sp = getSharedPreferences("code39_config", Activity.MODE_PRIVATE);
		editor = sp.edit();
		
		mini_state = sp.getBoolean("mini", false);
		maxi_state = sp.getBoolean("maxi", false);
		pre_state = sp.getBoolean("prefix", false);
		suf_state = sp.getBoolean("suffix", false);
		val_mini = sp.getInt("minilength", 0);
		val_maxi = sp.getInt("maxilength", 48);
		str_pre = sp.getString("str_pre", "");
		str_suf = sp.getString("str_suf", "");
		rg_id = sp.getInt("rg_check", R.id.rg_ncc);
		
		cp_code39.MinLength = val_mini;
		cp_code39.MaxLength = val_maxi;
		cp_code39.Prefix = pre_state ? 0x01 : 0x00;
		cp_code39.Suffix = suf_state ? 0x01 : 0x00;
		cp_code39.StrPrefix = str_pre;
		cp_code39.StrSuffix = str_suf;
		switch(rg_id) {
		case R.id.rg_ncc:
            cp_code39.Flags = 0x00;
			break;
		case R.id.rg_vdt:
			cp_code39.Flags = 0x01;
			break;
		case R.id.rg_vt:
			cp_code39.Flags = 0x02;
			break;
		default:
			break;
		}
		
		mScan.setSymbologyConfig(cp_code39);
		
		
		ActionBar actionBar=getActionBar();
		actionBar.setCustomView(R.layout.actionbar_layout);
		View actionBarLayout=actionBar.getCustomView();
		TextView settingTitle=(TextView) actionBarLayout.findViewById(R.id.title_actionBar);
		settingTitle.setText(getResources().getString(R.string.code_39));
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		
		cb_prefix = (CheckBox) findViewById(R.id.cb_code39_pre);
		cb_suffix = (CheckBox) findViewById(R.id.cb_code39_suf);
		cb_miniLen = (CheckBox) findViewById(R.id.cb_code39_mini);
		cb_maxiLen = (CheckBox) findViewById(R.id.cb_code39_maxi);
		tv_code39_pre = (TextView) findViewById(R.id.tv_code39_pre);
		tv_code39_suf = (TextView) findViewById(R.id.tv_code39_suf);
		tv_code39_mini = (TextView) findViewById(R.id.tv_code39_mini);
		tv_code39_maxi = (TextView) findViewById(R.id.tv_code39_maxi);
		
		cb_miniLen.setChecked(mini_state);
		cb_maxiLen.setChecked(maxi_state);
		cb_prefix.setChecked(pre_state);
		cb_suffix.setChecked(suf_state);
		tv_code39_mini.setText(val_mini+"");
		tv_code39_maxi.setText(val_maxi+"");
		tv_code39_pre.setText(str_pre);
		tv_code39_suf.setText(str_suf);
			
		cb_maxi = maxi_state;
		cb_mini = mini_state;
		cb_pre_state = pre_state;
		cb_suf_state = suf_state;
		
		cb_prefix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cp_code39.Prefix = 0x01;
					cb_pre_state = true;
				}else {
					cp_code39.Prefix = 0x00;
					cb_pre_state = false;
					cp_code39.StrPrefix = "";
					str_pre = "";
					editor.putString("str_pre", str_pre);
					tv_code39_pre.setText(str_pre);
				}
				editor.putBoolean("prefix", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_code39);
			}
		});
		
		cb_suffix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cp_code39.Suffix = 0x01;
					cb_suf_state = true;
				}else {
					cp_code39.Suffix = 0x00;
					cb_suf_state = false;
					cp_code39.StrSuffix = "";
					str_suf = "";
					editor.putString("str_suf", str_suf);
					tv_code39_suf.setText(str_suf);
				}
				editor.putBoolean("suffix", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_code39);
			}
		});
		
		cb_miniLen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cb_mini = true;
				}else {
					cp_code39.MinLength = 0;
					val_mini = 0;
					cb_mini = false;
					editor.putInt("minilength", val_mini);
					tv_code39_mini.setText(val_mini+"");
				}
				editor.putBoolean("mini", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_code39);
			}
		});
		
		cb_maxiLen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					cb_maxi = true;
				}else {
					cp_code39.MaxLength = 48;
					val_maxi = 48;
					cb_maxi = false;
					editor.putInt("maxilength", val_maxi);
					tv_code39_maxi.setText(val_maxi+"");
				}
				editor.putBoolean("maxi", isChecked);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_code39);
			}
		});
		
		RadioGroup radiogroup=(RadioGroup) findViewById(R.id.rg_code39);
		radiogroup.check(rg_id);
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rg_ncc:
                    cp_code39.Flags = 0x00;
					break;
				case R.id.rg_vdt:
					cp_code39.Flags = 0x01;
					break;
				case R.id.rg_vt:
					cp_code39.Flags = 0x02;
					break;
				default:
					break;
				}
				editor.putInt("rg_check", checkedId);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_code39);
			}
		});
	}

public void doClick(View view){
	switch (view.getId()) {
	case R.id.code39_mini_edit:
		//minimum's edit
		if(cb_mini) {
		 final EditText edittext1=new EditText(this);
		 edittext1.setInputType(InputType.TYPE_CLASS_NUMBER);
		 edittext1.setText(val_mini+"");
		 edittext1.setSelection((val_mini+"").length());
		new AlertDialog.Builder(this).setTitle("Please set an integer value (0-48):").setView(edittext1).setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int miniLen = Integer.parseInt(edittext1.getText().toString());
				//Toast.makeText(getApplicationContext(), "The MiniLen is (0~48)"+miniLen, 0).show();
				if((miniLen < 0) || (miniLen > 48)) {
					new AlertDialog.Builder(Code39Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub	
						}
					}).create().show();
				}else {
					val_mini = miniLen;
					cp_code39.MinLength = miniLen;
					editor.putInt("minilength", val_mini);
					editor.commit();
					tv_code39_mini.setText(val_mini+"");
				}

				mScan.setSymbologyConfig(cp_code39);
			}
		}).setNegativeButton("cancel",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create().show();
		}else {
			Toast.makeText(getApplicationContext(), "please select the MiniLen CheckBox", 0).show();
		}
		break;
	case R.id.code39_max_edit:
		//minimum's edit
		if(cb_maxi) {
		final EditText edittext2=new EditText(this);
		edittext2.setInputType(InputType.TYPE_CLASS_NUMBER);
		 edittext2.setText(val_maxi+"");
		 edittext2.setSelection((val_maxi+"").length());
new AlertDialog.Builder(this).setTitle("Please set an integer value (0-48):").setView(edittext2).setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int maxiLen = Integer.parseInt(edittext2.getText().toString());
				if((maxiLen < 0) || (maxiLen > 48)) {
					new AlertDialog.Builder(Code39Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub		
						}
					}).create().show();
				}else {
					val_maxi = maxiLen;
					cp_code39.MaxLength = maxiLen;
					editor.putInt("maxilength", val_maxi);
					editor.commit();
					tv_code39_maxi.setText(val_maxi+"");
				}
				
				mScan.setSymbologyConfig(cp_code39);
			}
		}).setNegativeButton("cancel",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create().show();
		}else {
			Toast.makeText(getApplicationContext(), "please select the MaxiLen CheckBox", 0).show();
		}
		break;
	case R.id.code39_mini_delete:
		//minimum's edit
		new AlertDialog.Builder(this).setTitle("Delete the manimum symbol ?").setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				cp_code39.MinLength = 0;
				val_mini = 0;
				mini_state = false;
				editor.putBoolean("mini_state", mini_state);
				editor.putInt("minilength", val_mini);
				editor.commit();
				tv_code39_mini.setText(val_mini+"");
				cb_miniLen.setChecked(mini_state);
				mScan.setSymbologyConfig(cp_code39);
			}
		}).setNegativeButton("cancel",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create().show();
		break;
	case R.id.code39_maxi_delete:
		//minimum's edit
		new AlertDialog.Builder(this).setTitle("Delete the maximum symbol ?").setPositiveButton("OK",new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							cp_code39.MaxLength = 48;
							val_maxi = 48;
							maxi_state = false;
							editor.putBoolean("maxi_state", maxi_state);
							editor.putInt("maxilength", val_maxi);
							editor.commit();
							tv_code39_maxi.setText(val_maxi+"");
							cb_maxiLen.setChecked(maxi_state);
							mScan.setSymbologyConfig(cp_code39);
						}
					}).setNegativeButton("cancel",new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create().show();
		break;

	case R.id.et_code39_prefix:
		//minimum's edit
		if(cb_pre_state) {
		final EditText edittext3=new EditText(this);
new AlertDialog.Builder(this).setTitle("please set the prefix that you want to edit").setView(edittext3).setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				str_pre = edittext3.getText().toString();
				tv_code39_pre.setText(str_pre);
				cp_code39.StrPrefix = str_pre;
				
				editor.putString("str_pre", str_pre);
				editor.commit();
				mScan.setSymbologyConfig(cp_code39);
			}
		}).setNegativeButton("cancel",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create().show();
		}else{
			Toast.makeText(getApplicationContext(), "please select the Prefix CheckBox", 0).show();
		}
		break;
	case R.id.et_code39_suffix:
		//minimum's edit
		if(cb_suf_state) {
		final EditText edittext4=new EditText(this);
new AlertDialog.Builder(this).setTitle("please set the suffix that you want to edit").setView(edittext4).setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				str_suf = edittext4.getText().toString();
				tv_code39_suf.setText(str_suf);
				cp_code39.StrSuffix = str_suf;
				
				editor.putString("str_suf", str_suf);
				editor.commit();
				
				mScan.setSymbologyConfig(cp_code39);
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
	case R.id.code39_prefix_delete:
		//minimum's edit
new AlertDialog.Builder(this).setTitle("Delete the prefix ?").setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				cp_code39.Prefix = 0x00;
				cp_code39.StrPrefix = "";
				mScan.setSymbologyConfig(cp_code39);
				
				pre_state = false;
				str_pre = "";
				editor.putString("str_pre", str_pre);
				editor.putBoolean("prefix", pre_state);
				editor.commit();
				cb_prefix.setChecked(pre_state);
				tv_code39_pre.setText(str_pre);
			}
		}).setNegativeButton("cancel",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create().show();
		break;
	case R.id.code39_suffix_delete:
		//minimum's edit
new AlertDialog.Builder(this).setTitle("Delete the suffix ?").setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				cp_code39.Suffix = 0x00;
				cp_code39.StrSuffix = "";
				mScan.setSymbologyConfig(cp_code39);

				suf_state = false;
				str_suf = "";
				editor.putString("str_suf", str_suf);
				editor.putBoolean("suffix", suf_state);
				editor.commit();
				cb_suffix.setChecked(suf_state);
				tv_code39_suf.setText(str_suf);
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
