package com.example.notepad;

import java.io.FileInputStream;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class ShowActivity extends Activity{

	private TextView showpanel;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        SharedPreferences mySharedPreference=getSharedPreferences("filesMTime",Activity.MODE_PRIVATE);
        SharedPreferences mySharedPreference2=getSharedPreferences("filesATime",Activity.MODE_PRIVATE);
        setTitle("Notepad");
        String title=this.getIntent().getStringExtra("Name");
        
        showpanel=(TextView)findViewById(R.id.textView);
        showpanel.setTextSize(5.0f);
        showpanel.setTextColor(Color.BLUE);
        showpanel.append("创建时间："+mySharedPreference2.getString(title, "")+"\n");
        showpanel.append("最后修改时间："+mySharedPreference.getString(title, "")+"\n\n");
        showpanel.setTextSize(20.0f);
        showpanel.setTextColor(Color.BLACK);
        showpanel.append(title+":\n");
        try {
			FileInputStream ins=openFileInput(title);
			byte[] buffer=new byte[ins.available()];
			ins.read(buffer);
			showpanel.append(new String(buffer));
			ins.close();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
