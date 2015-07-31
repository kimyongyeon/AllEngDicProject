package com.all.eng.dic.kyy.project;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;

public class AllEngDic_TabHost_Main extends TabActivity{
	TabHost tabHost;
	public static boolean tabFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE ); // 타이틀바 없애기
		setContentView(R.layout.allengdic_tabhost_main);

		tabHost = getTabHost();	
		TabHost.TabSpec spec;
		Intent intent;
		
		String str="";
		if(tabFlag == true){
			Intent i = getIntent();
			if (i != null) {
				str = i.getStringExtra("AllEngDic_Word");
			}
		}
		
		// HOME
		intent = new Intent().setClass(this, AllEngDic_Home.class);
		if(str.equals("")){
			spec = tabHost.newTabSpec("tab1").setIndicator("HOME",
					getResources().getDrawable(R.drawable.home))
					.setContent(intent);	
			tabHost.addTab(spec);	
		}else{
			intent.putExtra("AllEngDic_TabHost_Main", str);
			spec = tabHost.newTabSpec("tab1").setIndicator("HOME",
					getResources().getDrawable(R.drawable.home))
					.setContent(intent);	
			tabHost.addTab(spec);
		}
		// 나의 단어장
		intent = new Intent().setClass(this, AllEngDic_Word.class);
		intent.putExtra("AllEngDic_TabHost_Main", "2");
		spec = tabHost.newTabSpec("tab2").setIndicator("Word",
				getResources().getDrawable(R.drawable.word))
				.setContent(intent);
		tabHost.addTab(spec);
		// 셋팅
		intent = new Intent().setClass(this, AllEngDic_Setting.class);
		intent.putExtra("AllEngDic_TabHost_Main", "3");
		spec = tabHost.newTabSpec("tab3").setIndicator("Setting",
				getResources().getDrawable(R.drawable.setting))
				.setContent(intent);
		tabHost.addTab(spec);
	
		// 시작탭 설정
		tabHost.setCurrentTab(0);  
	}
}
