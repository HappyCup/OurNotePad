package com.example.notepad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class ViewHolder {
    TextView tv1;
    TextView tv2;
    TextView tv3;
}

public class MyListViewAdapater extends BaseAdapter{
	
	public List<String> mItemList;
	private Context context;
	SharedPreferences mySharedPreference;
	public MyListViewAdapater(Context context,List<String> objects) {
		this.context=context;
		mItemList=(List<String>)objects;
		mySharedPreference=context.getSharedPreferences("filesMTime",Activity.MODE_PRIVATE);
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			holder.tv1=(TextView)convertView.findViewById(R.id.tv);
			holder.tv2=(TextView)convertView.findViewById(R.id.tv_note);
			holder.tv3=(TextView)convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		}
		else{
			 holder=(ViewHolder)convertView.getTag();
		}
		
		String item=(String)getItem(position);
		String content=null;
		//Date time=null;
		//SimpleDateFormat format=new SimpleDateFormat("yyy年MM月dd日  HH:mm:ss ");
		String str=null;
		FileInputStream ins;
		try {
			File f=new File(item);
			//time=new Date(mySharedPreference.getLong(f.getName(), 0));
			str=mySharedPreference.getString(f.getName(), "");
			//time=new Date(f.lastModified());
			//str=format.format(time);
			//final Calendar mCalendar=Calendar.getInstance();
			//mCalendar.setTimeInMillis(f.lastModified());
			//Log.i("time::",mCalendar.getTime().toLocaleString());
			//str=mCalendar.getTime().toLocaleString();
			
			ins = context.openFileInput(f.getName());
			byte[] buffer=new byte[ins.available()];
			ins.read(buffer);
			content=new String(buffer);
			ins.close();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		holder.tv1.setText(item);
		holder.tv2.setText(content);
		holder.tv3.setText(str);
		return convertView;
	}

	public int getCount() {
		return mItemList.size();
	}

	public Object getItem(int position) {
		return mItemList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
}
