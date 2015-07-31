package com.all.eng.dic.kyy.project;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class AllEngDic_Favor extends Activity{
	ImageButton setBack;
	ListView lv;
	private AllEngDic_dbHandler db;
	private Vector<DTO_Address> vec = new Vector<DTO_Address>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allengdic_favor);
		setBack = (ImageButton) findViewById(R.id.setBack);
		
		listChkInit();
		
		setBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplication(), AllEngDic_Setting.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		Intent i = new Intent(getApplication(), AllEngDic_Setting.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}

	private void listChkInit(){
		db = new AllEngDic_dbHandler(this);
		vec = db.dataPotalRead();
		
		lv = (ListView)findViewById(R.id.setLv01);
		final WordAdapter sa = new WordAdapter(this, vec);
		lv.setAdapter(sa);
	}
	
	class WordAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private DTO_Address w;
		private Vector<DTO_Address> vec = new Vector<DTO_Address>();

		WordAdapter(Context context, Vector<DTO_Address> vec) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.vec = vec;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return vec.size();
		}

		public DTO_Address getItem(int position) {
			// TODO Auto-generated method stub
			return vec.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		TextView tv_str1;
		ImageButton ib;
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.allengdic_setting_row_list, parent, false);
			}
			
			tv_str1 = (TextView) convertView.findViewById(R.id.setting_rowTx01); // 단어
//			ib = (ImageButton) convertView.findViewById(R.id.setting_rowChk01); // 체크박스
			final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.setting_rowChk01); // 지움 체크박스

			w = new DTO_Address();
			w = vec.get(position);
			checkbox.setTag(position);
			checkbox.setChecked(w.isChk());
			tv_str1.setTag(position);
			tv_str1.setText(w.getName());
			
			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					int p = (Integer)checkbox.getTag();
					if(isChecked){
						chkYn(true, p);
					}else{
						chkYn(false, p);
					}
				}
			});

			return convertView;
		}
	}
	
	private void chkYn(boolean f, int p){
		DTO_Address addr = new DTO_Address();
		addr = vec.get(p);
		if(f){
			addr.setChk(true);
		}else{
			addr.setChk(false);
		}
		db.dataPotalUpdate(addr);
	}
}
