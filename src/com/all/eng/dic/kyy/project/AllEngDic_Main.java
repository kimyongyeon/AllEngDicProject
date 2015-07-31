package com.all.eng.dic.kyy.project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.LinearLayout;

public class AllEngDic_Main extends Activity {
	/** Called when the activity is first created. */
	LinearLayout ll;
	static Intent i;
	
	private String KEY_POPUP_CHECKED_PREFERENCE = "key_popup_checked_preference";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE ); // 타이틀바 없애기
		setContentView(R.layout.main);
		
		/*ll = (LinearLayout)findViewById(R.id.mainLL);
		ll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i = new Intent(AllEngDic_Main.this, AllEngDic_TabHost_Main.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
			}
		});*/
		
		SharedPreferences prefs = getSharedPreferences("checked", MODE_PRIVATE);
		String checked = prefs.getString(KEY_POPUP_CHECKED_PREFERENCE, "");
		
		if(checked.equals("")){
			prefs = getSharedPreferences("checked", MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(KEY_POPUP_CHECKED_PREFERENCE, "1");
			editor.commit();
			
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					// 이동
					i = new Intent(AllEngDic_Main.this, AllEngDic_Guide.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
					finish();
				}
			}, 500);
		}else{
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					// 이동
					i = new Intent(AllEngDic_Main.this, AllEngDic_Home.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
					finish();
				}
			}, 500);
		}
		
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
	}
}