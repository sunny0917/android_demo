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
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class PDF417Setting extends Activity {

	CodeParms cp_pdf417,cp_micropdf417;
	Scan mScan;
	
	private int val_mini = 0;
	private int val_maxi = 48;
	private String str_pre = "";
	private String str_suf = "";
	private TextView tv_pdf417_mini;
	private TextView tv_pdf417_maxi;
	private TextView tv_pdf417_pre;
	private TextView tv_pdf417_suf;
	
	private CheckBox cb_macropdf417;
	private CheckBox cb_micropdf417;
	private CheckBox cb_miniLen;
	private CheckBox cb_maxiLen;
	private CheckBox cb_prefix;
	private CheckBox cb_suffix;
	
	private boolean cb_mini = false;
	private boolean cb_maxi = false;
	private boolean cb_pre_state = false;
	private boolean cb_suf_state = false;
	private boolean macropdf417_state = false;
	private boolean micropdf417_state = false;
	private boolean mini_state = false;
	private boolean maxi_state = false;
	private boolean pre_state = false;
	private boolean suf_state = false;
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	private void codeParmsinit() {
		cp_pdf417 = new CodeParms(0x06);
		cp_pdf417.Flags = 0x00;
		cp_pdf417.MinLength = 1;
		cp_pdf417.MaxLength = 2750;
		cp_pdf417.Prefix = 0x00;
		cp_pdf417.Suffix = 0x00;
		cp_pdf417.StrPrefix = "";
		cp_pdf417.StrSuffix = "";
		cp_pdf417.upcToEan = 0x00;
		

		cp_micropdf417 = new CodeParms(0x08);
		cp_micropdf417.Flags = 0x00;
		cp_micropdf417.MinLength = 1;
		cp_micropdf417.MaxLength = 366;
		cp_micropdf417.Prefix = 0x00;
		cp_micropdf417.Suffix = 0x00;
		cp_micropdf417.StrPrefix = "";
		cp_micropdf417.StrSuffix = "";
		cp_micropdf417.upcToEan = 0x00;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf417_setting);
		
		codeParmsinit();
		
		sp = getSharedPreferences("pdf417_config", Activity.MODE_PRIVATE);
		editor = sp.edit();
		
		mini_state = sp.getBoolean("mini", false);
		maxi_state = sp.getBoolean("maxi", false);
		pre_state = sp.getBoolean("prefix", false);
		suf_state = sp.getBoolean("suffix", false);
		val_mini = sp.getInt("minilength", 1);
		val_maxi = sp.getInt("maxilength", 2750);
		str_pre = sp.getString("str_pre", "");
		str_suf = sp.getString("str_suf", "");
		macropdf417_state = sp.getBoolean("macropdf417", false);
		micropdf417_state = sp.getBoolean("micropdf417", false);
		
		mScan.honywareTypeOnOff("MicroPDF417", micropdf417_state ? 1 : 0);
		mScan.honywareTypeOnOff("MacroPDF417", macropdf417_state ? 1 : 0);
		
		if(micropdf417_state) {
			cp_micropdf417.MinLength = val_mini;
			cp_micropdf417.MaxLength = val_maxi;
			cp_micropdf417.Prefix = pre_state ? 0x01 : 0x00;
			cp_micropdf417.Suffix = suf_state ? 0x01 : 0x00;
			cp_micropdf417.StrPrefix = str_pre;
			cp_micropdf417.StrSuffix = str_suf;
			
			mScan.setSymbologyConfig(cp_micropdf417);
		}
		
		if(macropdf417_state)
		
		cp_pdf417.MinLength = val_mini;
		cp_pdf417.MaxLength = val_maxi;
		cp_pdf417.Prefix = pre_state ? 0x01 : 0x00;
		cp_pdf417.Suffix = suf_state ? 0x01 : 0x00;
		cp_pdf417.StrPrefix = str_pre;
		cp_pdf417.StrSuffix = str_suf;
		mScan.setSymbologyConfig(cp_pdf417);
		
		
		ActionBar actionBar=getActionBar();
		actionBar.setCustomView(R.layout.actionbar_layout);
		View actionBarLayout=actionBar.getCustomView();
		TextView settingTitle=(TextView) actionBarLayout.findViewById(R.id.title_actionBar);
		settingTitle.setText(getResources().getString(R.string.pdf_417));
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);

		cb_miniLen = (CheckBox) findViewById(R.id.cb_pdf417_mini);
		cb_maxiLen = (CheckBox) findViewById(R.id.cb_pdf417_maxi);
		cb_macropdf417=(CheckBox) findViewById(R.id.cb_macropdf417);
		cb_micropdf417=(CheckBox) findViewById(R.id.cb_micropdf417);
		cb_prefix = (CheckBox) findViewById(R.id.cb_pdf417_pre);
		cb_suffix = (CheckBox) findViewById(R.id.cb_pdf417_suf);
		tv_pdf417_mini = (TextView) findViewById(R.id.tv_pdf417_mini);
		tv_pdf417_maxi = (TextView) findViewById(R.id.tv_pdf417_maxi);
		tv_pdf417_pre = (TextView) findViewById(R.id.tv_pdf417_pre);
		tv_pdf417_suf = (TextView) findViewById(R.id.tv_pdf417_suf);
		
		cb_miniLen.setChecked(mini_state);
		cb_maxiLen.setChecked(maxi_state);
		cb_macropdf417.setChecked(macropdf417_state);
		cb_micropdf417.setChecked(micropdf417_state);
		cb_prefix.setChecked(pre_state);
		cb_suffix.setChecked(suf_state);
		tv_pdf417_mini.setText(val_mini+"");
		tv_pdf417_maxi.setText(val_maxi+"");
		tv_pdf417_pre.setText(str_pre);
		tv_pdf417_suf.setText(str_suf);
		
		cb_maxi = maxi_state;
		cb_mini = mini_state;
		cb_pre_state = pre_state;
		cb_suf_state = suf_state;
		
		cb_macropdf417.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					macropdf417_state = true;
				}
				else{
					macropdf417_state = false;
				}
				editor.putBoolean("macropdf417", macropdf417_state);
				editor.commit();

				mScan.honywareTypeOnOff("MacroPDF417", macropdf417_state?1:0);
			}
		});
		cb_micropdf417.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					micropdf417_state = true;
				}
				else{
					micropdf417_state = false;
				}
				editor.putBoolean("micropdf417", micropdf417_state);
				editor.commit();

				mScan.honywareTypeOnOff("MicroPDF417", micropdf417_state?1:0);
			}
		});
		
		cb_prefix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					if(micropdf417_state) {
						cp_micropdf417.Prefix = 0x01;
					}
					cp_pdf417.Prefix = 0x01;
					cb_pre_state = true;
				}else {
					if(micropdf417_state) {
						cp_micropdf417.Prefix = 0x00;
						cp_micropdf417.StrPrefix = "";
					}
					
					cp_pdf417.Prefix = 0x00;
					cb_pre_state = false;
					cp_pdf417.StrPrefix = "";
					str_pre = "";
					editor.putString("str_pre", str_pre);
					tv_pdf417_pre.setText(str_pre);
				}
				editor.putBoolean("prefix", isChecked);
				editor.commit();
				if(micropdf417_state) {
					mScan.setSymbologyConfig(cp_micropdf417);
				}
				
				mScan.setSymbologyConfig(cp_pdf417);
			}
		});
		
		cb_suffix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					if(micropdf417_state) {
						cp_micropdf417.Suffix = 0x01;
					}
					cp_pdf417.Suffix = 0x01;
					cb_suf_state = true;
				}else {
					if(micropdf417_state) {
						cp_micropdf417.Suffix = 0x00;
						cp_micropdf417.StrSuffix = "";
					}
					cp_pdf417.Suffix = 0x00;
					cb_suf_state = false;
					cp_pdf417.StrSuffix = "";
					str_suf = "";
					editor.putString("str_suf", str_suf);
					tv_pdf417_suf.setText(str_suf);
				}
				editor.putBoolean("suffix", isChecked);
				editor.commit();
				
				if(micropdf417_state)
					mScan.setSymbologyConfig(cp_micropdf417);
				
				mScan.setSymbologyConfig(cp_pdf417);
			}
		});
		
		cb_miniLen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					cb_mini = true;
				}
				else{
					if(micropdf417_state) {
						cp_micropdf417.MinLength = 1;
					}
					cp_pdf417.MinLength = 1;
					val_mini = 1;
					cb_mini = false;
					editor.putInt("minilength", val_mini);
					tv_pdf417_mini.setText(val_mini+"");
				}
				editor.putBoolean("mini", isChecked);
				editor.commit();
				
				if(micropdf417_state)
					mScan.setSymbologyConfig(cp_micropdf417);
				
				mScan.setSymbologyConfig(cp_pdf417);
			}
		});
		cb_maxiLen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					cb_maxi = true;
				}
				else{
					if(micropdf417_state) {
						cp_micropdf417.MaxLength = 366;
					}
					cp_pdf417.MaxLength = 2750;
					if(micropdf417_state)
						val_maxi = 366;
					else
						val_maxi = 2750;
					cb_maxi = false;
					editor.putInt("maxilength", val_maxi);
					tv_pdf417_maxi.setText(val_maxi+"");
				}
				editor.putBoolean("maxi", isChecked);
				editor.commit();
				
				if(micropdf417_state)
					mScan.setSymbologyConfig(cp_micropdf417);
				
				mScan.setSymbologyConfig(cp_pdf417);
			}
		});
	}
	public void doClick(View view){
		switch (view.getId()) {
		case R.id.et_pdf417_mini:
			//minimum's edit
			if(cb_mini) {
			 final EditText edittext1=new EditText(this);
			 edittext1.setInputType(InputType.TYPE_CLASS_NUMBER);
			 edittext1.setText(val_mini+"");
			 edittext1.setSelection((val_mini+"").length());
			new AlertDialog.Builder(this).setTitle("set value pdf417(1-2750) micropdf417(1-366):").setView(edittext1).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					int miniLen = Integer.parseInt(edittext1.getText().toString());
					//Toast.makeText(getApplicationContext(), "The MiniLen is (0~48)"+miniLen, 0).show();
					if(micropdf417_state) {
						if((miniLen < 1) || (miniLen > 366)) {
							new AlertDialog.Builder(PDF417Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub	
								}
							}).create().show();
						}else {
							val_mini = miniLen;
							cp_micropdf417.MinLength = miniLen;
							editor.putInt("minilength", val_mini);
							editor.commit();
							tv_pdf417_mini.setText(val_mini+"");
						}
						mScan.setSymbologyConfig(cp_micropdf417);
					}
					if((miniLen < 1) || (miniLen > 2750)) {
						new AlertDialog.Builder(PDF417Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub	
							}
						}).create().show();
					}else {
						val_mini = miniLen;
						cp_pdf417.MinLength = miniLen;
						editor.putInt("minilength", val_mini);
						editor.commit();
						tv_pdf417_mini.setText(val_mini+"");
					}
					mScan.setSymbologyConfig(cp_pdf417);
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
		case R.id.et_pdf417_maxi:
			//minimum's edit
			if(cb_maxi) {
			final EditText edittext2=new EditText(this);
			edittext2.setInputType(InputType.TYPE_CLASS_NUMBER);
			 edittext2.setText(val_maxi+"");
			 edittext2.setSelection((val_maxi+"").length());
	new AlertDialog.Builder(this).setTitle("set value pdf417(1-2750) micropdf417(1-366):").setView(edittext2).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					int maxiLen = Integer.parseInt(edittext2.getText().toString());
					if(micropdf417_state) {
						if((maxiLen < 1) || (maxiLen > 366)) {
							new AlertDialog.Builder(PDF417Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub		
								}
							}).create().show();
						}else {
							val_maxi = maxiLen;
							cp_micropdf417.MaxLength = maxiLen;
							editor.putInt("maxilength", val_maxi);
							editor.commit();
							tv_pdf417_maxi.setText(val_maxi+"");
						}
						mScan.setSymbologyConfig(cp_micropdf417);
					}
					if((maxiLen < 1) || (maxiLen > 2750)) {
						new AlertDialog.Builder(PDF417Setting.this).setTitle("The value is not valid.").setPositiveButton("OK", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub		
							}
						}).create().show();
					}else {
						val_maxi = maxiLen;
						cp_pdf417.MaxLength = maxiLen;
						editor.putInt("maxilength", val_maxi);
						editor.commit();
						tv_pdf417_maxi.setText(val_maxi+"");
					}
					
					mScan.setSymbologyConfig(cp_pdf417);
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
		case R.id.et_pdf417_mini_delete:
			//minimum's edit
			new AlertDialog.Builder(this).setTitle("Delete the manimum symbol ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(micropdf417_state) {
						cp_micropdf417.MinLength = 1;
						mScan.setSymbologyConfig(cp_micropdf417);
					}
					val_mini = 1;
					mini_state = false;
					editor.putBoolean("mini_state", mini_state);
					editor.putInt("minilength", val_mini);
					editor.commit();
					tv_pdf417_mini.setText(val_mini+"");
					cb_miniLen.setChecked(mini_state);
					
					cp_pdf417.MinLength = 1;
					mScan.setSymbologyConfig(cp_pdf417);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			break;
		case R.id.et_pdf417_maxi_delete:
			//minimum's edit
			new AlertDialog.Builder(this).setTitle("Delete the maximum symbol ?").setPositiveButton("OK",new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(micropdf417_state) {
									cp_micropdf417.MaxLength = 366;
									val_maxi = 366;
									mScan.setSymbologyConfig(cp_micropdf417);
								}
								val_maxi = 2750;
								maxi_state = false;
								editor.putBoolean("maxi_state", maxi_state);
								editor.putInt("maxilength", val_maxi);
								editor.commit();
								tv_pdf417_maxi.setText(val_maxi+"");
								cb_maxiLen.setChecked(maxi_state);
								
								cp_pdf417.MaxLength = 2750;
								mScan.setSymbologyConfig(cp_pdf417);
							}
						}).setNegativeButton("cancel",new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						}).create().show();
			break;
		case R.id.et_pdf417_prefix:
			//minimum's edit
			if(cb_pre_state) {
			final EditText edittext3=new EditText(this);
	new AlertDialog.Builder(this).setTitle("please set the prefix that you want to edit").setView(edittext3).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					str_pre = edittext3.getText().toString();
					tv_pdf417_pre.setText(str_pre);
					if(micropdf417_state)
						cp_micropdf417.StrPrefix = str_pre;
					
					cp_pdf417.StrPrefix = str_pre;
					
					editor.putString("str_pre", str_pre);
					editor.commit();
					
					if(micropdf417_state)
						mScan.setSymbologyConfig(cp_micropdf417);
					mScan.setSymbologyConfig(cp_pdf417);
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
		case R.id.et_pdf417_suffix:
			//minimum's edit
			if(cb_suf_state) {
			final EditText edittext4=new EditText(this);
	new AlertDialog.Builder(this).setTitle("please set the suffix that you want to edit").setView(edittext4).setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					str_suf = edittext4.getText().toString();
					tv_pdf417_suf.setText(str_suf);
					if(micropdf417_state)
						cp_micropdf417.StrSuffix = str_suf;
					cp_pdf417.StrSuffix = str_suf;
					
					editor.putString("str_suf", str_suf);
					editor.commit();
					
					if(micropdf417_state)
						mScan.setSymbologyConfig(cp_micropdf417);
					mScan.setSymbologyConfig(cp_pdf417);
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
		case R.id.et_pdf417_prefix_delete:
			//minimum's edit
	new AlertDialog.Builder(this).setTitle("Delete the prefix ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//if(micropdf417_state) {
						cp_micropdf417.Prefix = 0x00;
						cp_micropdf417.StrPrefix = "";
						mScan.setSymbologyConfig(cp_micropdf417);
					//}
					cp_pdf417.Prefix = 0x00;
					cp_pdf417.StrPrefix = "";
					mScan.setSymbologyConfig(cp_pdf417);
					
					pre_state = false;
					str_pre = "";
					editor.putString("str_pre", str_pre);
					editor.putBoolean("prefix", pre_state);
					editor.commit();
					cb_prefix.setChecked(pre_state);
					tv_pdf417_pre.setText(str_pre);
				}
			}).setNegativeButton("cancel",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			break;
		case R.id.et_pdf417_suffix_delete:
			//minimum's edit
	new AlertDialog.Builder(this).setTitle("Delete the suffix ?").setPositiveButton("OK",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//if(micropdf417_state) {
						cp_micropdf417.Suffix = 0x00;
						cp_micropdf417.StrSuffix = "";
						mScan.setSymbologyConfig(cp_micropdf417);
					//}
					cp_pdf417.Suffix = 0x00;
					cp_pdf417.StrSuffix = "";
					mScan.setSymbologyConfig(cp_pdf417);

					suf_state = false;
					str_suf = "";
					editor.putString("str_suf", str_suf);
					editor.putBoolean("suffix", suf_state);
					editor.commit();
					cb_suffix.setChecked(suf_state);
					tv_pdf417_suf.setText(str_suf);
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
