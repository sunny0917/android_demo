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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Interleaved2af5Setting extends Activity {

	CodeParms cp_Interleaved2af5;
	Scan mScan;
	
	private int val_mini = 4;
	private int val_maxi = 80;
	private String str_pre = "";
	private String str_suf = "";
	private TextView tv_interleaved2af5_mini;
	private TextView tv_interleaved2af5_maxi;
	private TextView tv_interleaved2af5_pre;
	private TextView tv_interleaved2af5_suf;
	
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
		cp_Interleaved2af5 = new CodeParms(0x03);
		cp_Interleaved2af5.Flags = 0x00;
		cp_Interleaved2af5.MinLength = 4;
		cp_Interleaved2af5.MaxLength = 80;
		cp_Interleaved2af5.Prefix = 0x00;
		cp_Interleaved2af5.Suffix = 0x00;
		cp_Interleaved2af5.StrPrefix = "";
		cp_Interleaved2af5.StrSuffix = "";
		cp_Interleaved2af5.upcToEan = 0x00;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interleaved2af5_setting);
		
		codeParmsinit();
		
		sp = getSharedPreferences("interleaved2af5_config", Activity.MODE_PRIVATE);
		editor = sp.edit();
		
		mini_state = sp.getBoolean("mini", false);
		maxi_state = sp.getBoolean("maxi", false);
		pre_state = sp.getBoolean("prefix", false);
		suf_state = sp.getBoolean("suffix", false);
		val_mini = sp.getInt("minilength", 4);
		val_maxi = sp.getInt("maxilength", 80);
		str_pre = sp.getString("str_pre", str_pre);
		str_suf = sp.getString("str_suf", str_suf);
		rg_id = sp.getInt("rg_check", R.id.rg_interleaved2af5_ncc);
		
		cp_Interleaved2af5.MinLength = val_mini;
		cp_Interleaved2af5.MaxLength = val_maxi;
		cp_Interleaved2af5.Prefix = pre_state ? 0x01 : 0x00;
		cp_Interleaved2af5.Suffix = suf_state ? 0x01 : 0x00;
		cp_Interleaved2af5.StrPrefix = str_pre;
		cp_Interleaved2af5.StrSuffix = str_suf;
		switch(rg_id) {
		case R.id.rg_interleaved2af5_ncc:
			cp_Interleaved2af5.Flags = 0x00;
			break;
		case R.id.rg_interleaved2af5_vdt:
			cp_Interleaved2af5.Flags = 0x01;
			break;
		case R.id.rg_interleaved2af5_vt:
			cp_Interleaved2af5.Flags = 0x02;
			break;
		default:
			break;
		}
		
		mScan.setSymbologyConfig(cp_Interleaved2af5);
		
		 ActionBar actionBar=getActionBar();
	     actionBar.setCustomView(R.layout.actionbar_layout);
	        View actionBarLayout=actionBar.getCustomView();
	        TextView settingTitle=(TextView) actionBarLayout.findViewById(R.id.title_actionBar);
	        settingTitle.setText(getResources().getString(R.string.interleaved_2of5));
	        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	        actionBar.setDisplayShowCustomEnabled(true);
	        
	        cb_prefix = (CheckBox) findViewById(R.id.cb_interleaved2af5_pre);
			cb_suffix = (CheckBox) findViewById(R.id.cb_interleaved2af5_suf);
			cb_miniLen = (CheckBox) findViewById(R.id.cb_interleaved2af5_mini);
			cb_maxiLen = (CheckBox) findViewById(R.id.cb_interleaved2af5_maxi);
			tv_interleaved2af5_mini = (TextView) findViewById(R.id.tv_interleaved2af5_mini);
			tv_interleaved2af5_maxi = (TextView) findViewById(R.id.tv_interleaved2af5_maxi);
			tv_interleaved2af5_pre = (TextView) findViewById(R.id.tv_interleaved2af5_pre);
			tv_interleaved2af5_suf = (TextView) findViewById(R.id.tv_interleaved2af5_suf);
			
			cb_miniLen.setChecked(mini_state);
			cb_maxiLen.setChecked(maxi_state);
			cb_prefix.setChecked(pre_state);
			cb_suffix.setChecked(suf_state);
			tv_interleaved2af5_mini.setText(val_mini+"");
			tv_interleaved2af5_maxi.setText(val_maxi+"");
			tv_interleaved2af5_pre.setText(str_pre);
			tv_interleaved2af5_suf.setText(str_suf);
			
			cb_maxi = maxi_state;
			cb_mini = mini_state;
			cb_pre_state = pre_state;
			cb_suf_state = suf_state;
			
			cb_prefix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked) {
						cp_Interleaved2af5.Prefix = 0x01;
						cb_pre_state = true;
					}else {
						cp_Interleaved2af5.Prefix = 0x00;
						cb_suf_state = false;
						cp_Interleaved2af5.StrPrefix = "";
						str_pre = "";
						
						editor.putString("str_pre", str_pre);
						tv_interleaved2af5_pre.setText(str_pre);
					}
					pre_state = isChecked;
					editor.putBoolean("prefix", isChecked);
					editor.commit();
					
					mScan.setSymbologyConfig(cp_Interleaved2af5);
				}
			});
			
			cb_suffix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked) {
						cp_Interleaved2af5.Suffix = 0x01;
						cb_suf_state = true;
					}else {
						cp_Interleaved2af5.Suffix = 0x00;
						cb_suf_state = false;
						cp_Interleaved2af5.StrSuffix = "";
						str_suf = "";
						editor.putString("str_suf", str_suf);
						tv_interleaved2af5_suf.setText(str_suf);
					}
					suf_state = isChecked;
					editor.putBoolean("suffix", isChecked);
					editor.commit();
					
					mScan.setSymbologyConfig(cp_Interleaved2af5);
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
						cp_Interleaved2af5.MinLength = 4;
						val_mini = 4;
						editor.putInt("minilength", val_mini);
						tv_interleaved2af5_mini.setText(val_mini+"");
					}
					mini_state = isChecked;
					editor.putBoolean("mini", isChecked);
					editor.commit();
					
					mScan.setSymbologyConfig(cp_Interleaved2af5);
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
						cp_Interleaved2af5.MaxLength = 80;
						val_maxi = 80;
						editor.putInt("maxilength", val_maxi);
						tv_interleaved2af5_maxi.setText(val_maxi+"");
					}
					maxi_state = isChecked;
					editor.putBoolean("maxi", isChecked);
					editor.commit();
					
					mScan.setSymbologyConfig(cp_Interleaved2af5);
				}
			});
	        
	        RadioGroup radiogroup=(RadioGroup) findViewById(R.id.rg_interleaved2af5);
	        radiogroup.check(rg_id);
			radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					switch (checkedId) {
					case R.id.rg_interleaved2af5_ncc:
	                    cp_Interleaved2af5.Flags = 0x00;
						break;
					case R.id.rg_interleaved2af5_vdt:
	                    cp_Interleaved2af5.Flags = 0x01;
						break;
					case R.id.rg_interleaved2af5_vt:
	                    cp_Interleaved2af5.Flags = 0x02;
						break;
					default:
						break;
					}
					editor.putInt("rg_check", checkedId);
					editor.commit();
					
					mScan.setSymbologyConfig(cp_Interleaved2af5);
				}
			});
			
		}
	public void doClick(View view){
		switch (view.getId()) {
		case R.id.et_interleaved2af5_mini:
			//minimum's edit
			if(cb_mini) {
			 final EditText edittext1=new EditText(this);
			 edittext1.setInputType(InputType.TYPE_CLASS_NUMBER);
			 edittext1.setText(val_mini+"");
			 edittext1.setSelection((val_mini+"").length());
			new AlertDialog.Builder(this).setTitle("Please set an integer value (4-80):").setView(edittext1).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					int miniLen = Integer.parseInt(edittext1.getText().toString());
					//Toast.makeText(getApplicationContext(), "The MiniLen is (4~80)"+miniLen, 0).show();
					if((miniLen < 4) || (miniLen > 80)) {
						new AlertDialog.Builder(Interleaved2af5Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
							}
						}).create().show();
					}else {
						cp_Interleaved2af5.MinLength = miniLen;
						val_mini = miniLen;
						editor.putInt("minilength", val_mini);
						editor.commit();
						tv_interleaved2af5_mini.setText(val_mini+"");
					}
					mScan.setSymbologyConfig(cp_Interleaved2af5);
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
		case R.id.et_interleaved2af5_maxi:
			//minimum's edit
			if(cb_maxi) {
			final EditText edittext2=new EditText(this);
			edittext2.setInputType(InputType.TYPE_CLASS_NUMBER);
			 edittext2.setText(val_maxi+"");
			 edittext2.setSelection((val_maxi+"").length());
	new AlertDialog.Builder(this).setTitle("Please set an integer value (4-80):").setView(edittext2).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					int maxiLen = Integer.parseInt(edittext2.getText().toString());
					if((maxiLen < 4) || (maxiLen > 80)) {
						new AlertDialog.Builder(Interleaved2af5Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub		
							}
						}).create().show();
					}else {
						val_maxi = maxiLen;
						cp_Interleaved2af5.MaxLength = maxiLen;
						editor.putInt("maxilength", val_maxi);
						editor.commit();
						tv_interleaved2af5_maxi.setText(val_maxi+"");
					}
					
					mScan.setSymbologyConfig(cp_Interleaved2af5);
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
		case R.id.interleavde_mini_delete:
			//minimum's edit
			new AlertDialog.Builder(this).setTitle("Delete the manimum symbol ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cp_Interleaved2af5.MinLength = 4;
					val_mini = 4;
					mini_state = false;
					editor.putBoolean("mini_state", mini_state);
					editor.putInt("minilength", val_mini);
					editor.commit();
					tv_interleaved2af5_mini.setText(val_mini+"");
					cb_miniLen.setChecked(mini_state);
					mScan.setSymbologyConfig(cp_Interleaved2af5);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			break;
		case R.id.interleavde_maxi_delete:
			//minimum's edit
			new AlertDialog.Builder(this).setTitle("Delete the maximum symbol ?").setPositiveButton("OK",new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								cp_Interleaved2af5.MaxLength = 80;
								val_maxi = 80;
								maxi_state = false;
								editor.putBoolean("maxi_state", maxi_state);
								editor.putInt("maxilength", val_maxi);
								editor.commit();
								tv_interleaved2af5_maxi.setText(val_maxi+"");
								cb_maxiLen.setChecked(maxi_state);
								mScan.setSymbologyConfig(cp_Interleaved2af5);
							}
						}).setNegativeButton("cancel",new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						}).create().show();
			break;

		case R.id.et_interleaved2af5_prefix:
			//minimum's edit
			if(cb_pre_state) {
			final EditText edittext3=new EditText(this);
	new AlertDialog.Builder(this).setTitle("please set the prefix that you want to edit").setView(edittext3).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					str_pre = edittext3.getText().toString();
					cp_Interleaved2af5.StrPrefix = str_pre;
					tv_interleaved2af5_pre.setText(str_pre);
					
					editor.putString("str_pre", str_pre);
					editor.commit();
					mScan.setSymbologyConfig(cp_Interleaved2af5);
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
		case R.id.et_interleaved2af5_suffix:
			//minimum's edit
			if(cb_suf_state) {
			final EditText edittext4=new EditText(this);
	new AlertDialog.Builder(this).setTitle("please set the suffix that you want to edit").setView(edittext4).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					str_suf = edittext4.getText().toString();
					cp_Interleaved2af5.StrSuffix = str_suf;
					tv_interleaved2af5_suf.setText(str_suf);
					
					editor.putString("str_suf", str_suf);
					editor.commit();
					mScan.setSymbologyConfig(cp_Interleaved2af5);
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
		case R.id.interleaved2af5_prefix_delete:
			//minimum's edit
	new AlertDialog.Builder(this).setTitle("Delete the prefix ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cp_Interleaved2af5.Prefix = 0x00;
					cp_Interleaved2af5.StrPrefix = "";
					str_pre = "";
					tv_interleaved2af5_pre.setText(str_pre);
					editor.putString("str_pre", str_pre);
					mScan.setSymbologyConfig(cp_Interleaved2af5);
					
					pre_state = false;
					editor.putBoolean("prefix", pre_state);
					editor.commit();
					cb_prefix.setChecked(pre_state);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			break;
		case R.id.interleaved2af5_suffix_delete:
			//minimum's edit
	new AlertDialog.Builder(this).setTitle("Delete the suffix ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cp_Interleaved2af5.Suffix = 0x00;
					cp_Interleaved2af5.StrSuffix = "";
					str_suf = "";
					tv_interleaved2af5_suf.setText(str_suf);
					editor.putString("str_suf", str_suf);
					mScan.setSymbologyConfig(cp_Interleaved2af5);
					
					suf_state = false;
					editor.putBoolean("suffix", suf_state);
					editor.commit();
					cb_suffix.setChecked(suf_state);
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
