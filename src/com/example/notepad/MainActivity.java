package com.example.notepad;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button newnote;
	private ListView myNoteList;
	private MyListViewAdapater adapter;
	SharedPreferences mySharedPreference;
	SharedPreferences mySharedPreference2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mySharedPreference=getSharedPreferences("filesMTime",Activity.MODE_PRIVATE);
        mySharedPreference2=getSharedPreferences("filesATime",Activity.MODE_PRIVATE);
        
        setTitle("Notepad");
        newnote=(Button)findViewById(R.id.newnote);
        myNoteList=(ListView)findViewById(R.id.noteList);
        newnote.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				startActivityForResult(new Intent(MainActivity.this,editActivity.class),0);
			}});
        
        List<String> notes=new ArrayList<String>();
        for(int i=0;i<fileList().length;i++){
        	notes.add(fileList()[i]);
        }
        adapter=new MyListViewAdapater(this,notes);
        myNoteList.setAdapter(adapter);
        myNoteList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				ViewHolder holder=(ViewHolder)arg1.getTag();
		    	TextView p=holder.tv1;
				Intent intent=new Intent(MainActivity.this,ShowActivity.class);
				intent.putExtra("Name", p.getText());
				startActivity(intent);
			}});
        
        this.registerForContextMenu(myNoteList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
    	super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE,0,0,R.string.menu_settings);
        return true;
    }
    
    @Override
	public boolean onMenuItemSelected(int featureId,MenuItem item){
		switch(item.getItemId()){
		case 0:
			System.exit(0);
			break;
		default:
			//System.exit(0);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,  
    		            ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.setHeaderTitle("Menu");
    	menu.add(0, 3, Menu.NONE, "查看");
    	menu.add(0, 4, Menu.NONE, "编辑");
    	menu.add(0, Menu.FIRST+1, Menu.NONE, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	Log.i("菜单项ID:", " "+(int)info.id);
    	Log.i("菜单项长度:", " "+myNoteList.getCount());
    	ViewHolder holder=(ViewHolder)info.targetView.getTag();
    	//ViewHolder holder=(ViewHolder)myNoteList.getChildAt((int) info.id).getTag();//菜单过长时会出错！
    	TextView ed=holder.tv1;
    	String filename=(String) ed.getText();
    	
    	switch(item.getItemId()){  
    	
    	case 3:
    		Intent intent=new Intent(MainActivity.this,ShowActivity.class);
    		intent.putExtra("Name", filename);
    		startActivity(intent);
    		break;
    	case 2:
    		deleteFile(filename);
    		mySharedPreference.edit().remove(filename);
    		mySharedPreference.edit().commit();
    		mySharedPreference2.edit().remove(filename);
    		mySharedPreference2.edit().commit();
    		updateList();
    		break;
    	case 4:
    		Intent intent2=new Intent(MainActivity.this,editActivity.class);
    		intent2.putExtra("Name", filename);
    		startActivityForResult(intent2,0);
    		break;
        default:
        	return super.onContextItemSelected(item);  
    	}  
    	return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (resultCode) {
    	case RESULT_OK:
    		updateList();
    		break;
    	default:
    		break;	
    	}
    }
    
    public void updateList(){
    	List<String> notes=new ArrayList<String>();
        for(int i=0;i<fileList().length;i++)
        	notes.add(fileList()[i]);
        adapter.mItemList=notes;
        adapter.notifyDataSetChanged();
    }
}



