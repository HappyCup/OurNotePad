package com.example.notepad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class editActivity extends Activity{

	private EditText title;
	private EditText content;
	private File file=null;
	private String filename=null;
	private boolean isEdited=false;
	private SharedPreferences.Editor editor;
	private SharedPreferences.Editor editor2;
	SharedPreferences mySharedPreference2;
	private SimpleDateFormat format;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        
        SharedPreferences mySharedPreference=getSharedPreferences("filesMTime",Activity.MODE_PRIVATE);
        editor=mySharedPreference.edit();
        mySharedPreference2=getSharedPreferences("filesATime",Activity.MODE_PRIVATE);
        editor2=mySharedPreference2.edit();
        format=new SimpleDateFormat("yyy年MM月dd日  HH:mm:ss ");
        
        setTitle("Notepad");
        title=(EditText)findViewById(R.id.headline);
        content=(EditText)findViewById(R.id.content);
        filename=this.getIntent().getStringExtra("Name");
        if(filename!=null){
        	title.setText(filename);
        	file=new File(filename);
        	try {
        		FileInputStream ins=openFileInput(filename);
    			byte[] buffer=new byte[ins.available()];
    			ins.read(buffer);
    			content.setText(new String(buffer));
    			ins.close();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
        }
        setResult(RESULT_OK,null);
        
        title.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable arg0) {
				// TODO 自动生成的方法存根
				isEdited=true;
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				
			}
        	
        });
        content.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				isEdited=true;
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				
			}
        	
        });
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE,0,0,R.string.edit_menu_save);
        menu.add(Menu.NONE,1,0,R.string.edit_menu_abandon);
        return true;
    }
	
	@Override
	public boolean onMenuItemSelected(int featureId,MenuItem item){
		if(!title.getText().toString().equals(filename)){
			if(file!=null){
				editor.remove(file.getName());
				editor.commit();
				String st=mySharedPreference2.getString(file.getName(), "");
				editor2.remove(file.getName()); 
				editor2.putString(title.getText().toString(),st);
				editor2.commit();
				deleteFile(file.getName());
			}
			else{
				long t=System.currentTimeMillis(); 
				editor2.putString(title.getText().toString(),format.format(new Date(t)));
				editor2.commit();
			}
			filename=title.getText().toString();
		    file=new File(title.getText().toString());
		}
		FileOutputStream fos=null;
		try{
			fos=openFileOutput(file.getName(),Activity.MODE_PRIVATE);
		}catch(Exception e){
			e.printStackTrace();
		}
		switch(item.getItemId()){
		case 0:
			try {
				//Time t=new Time(); 
				//t.setToNow();
				long t=System.currentTimeMillis();         //获取系统时间
				//final Calendar mCalendar=Calendar.getInstance();
				//mCalendar.setTimeInMillis(t);
				editor.putString(file.getName(),format.format(new Date(t)));// mCalendar.getTime().toLocaleString());
				editor.commit();
				//Log.i("time::",mCalendar.getTime().toLocaleString());
				
				
				fos.write(content.getText().toString().getBytes());
				fos.close();
				//file.setLastModified(mCalendar.getTime().getTime());
				//mCalendar.setTimeInMillis(file.lastModified());
				//Log.i("time::",mCalendar.getTime().toLocaleString());
				
				//editor.putLong(file.getName(), t);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			isEdited=false;
			return true;
		case 1:
			if(file!=null){
				deleteFile(file.getName());
				editor.remove(file.getName());
				editor.commit();
				editor2.remove(file.getName());
				editor2.commit();
			}
			finish();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // 如果是返回键,直接返回到桌面
	        if((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)&&isEdited==true){
	        	
	        	final AlertDialog dlg = new AlertDialog.Builder(this).create();
	            dlg.show();
	            Window window = dlg.getWindow();
	            window.setContentView(R.layout.exit_sure);
	            window.getAttributes().alpha=0.5f;
	            Button ok = (Button) window.findViewById(R.id.want_abandon);
	            ok.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						//if(file!=null)
							//deleteFile(file.getName());
						finish();
					}
	            });

	            Button cancel = (Button) window.findViewById(R.id.want_cancel);
	            cancel.setOnClickListener(new View.OnClickListener() {
	                public void onClick(View v) {
	                    dlg.cancel();
	                }
	            });

	        }
	        return super.onKeyDown(keyCode, event);
	    }

}
