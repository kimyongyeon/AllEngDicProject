package com.all.eng.dic.kyy.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AllEngDic_Guide extends Activity{
	private final int COUNT=2;
	private ViewPager mPager;	//�� ������
	private LinearLayout ll;
	
	private OnClickListener mButtonClick = new OnClickListener() {		//Ŭ�� �̺�Ʈ ��ü
		public void onClick(View v) {
			String text = ((Button)v).getText().toString();		//��ư���� �̺�Ʈ�� �����. ��ư ���� ������.
			Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();	//�佺Ʈ�� ���
		}
	};
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		Intent i = new Intent(AllEngDic_Guide.this, AllEngDic_Setting.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));
	}
	
	//Pager �ƴ��� ����
	private class BkPagerAdapter extends PagerAdapter{
		private LayoutInflater mInflater;

		public BkPagerAdapter( Context con) {
			super();
			mInflater = LayoutInflater.from(con);
		}

		@Override public int getCount() { return COUNT; }	//���⼭�� 2���� �� ���̴�.

		//������������ ����� �䰴ü ����/���
		@Override public Object instantiateItem(View pager, int position) {
			View v = null;
			if(position==0){
				v = mInflater.inflate(R.layout.allengdic_guild_1, null);
			}
			else{
				v = mInflater.inflate(R.layout.allengdic_guild_2, null);
				ll = (LinearLayout)v.findViewById(R.id.guide_ll);
				ll.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(AllEngDic_Home.m_HelpPageFlag){
							Intent i = new Intent(AllEngDic_Guide.this, AllEngDic_Home.class);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(i);
							finish();
						}else{
							Intent i = new Intent(AllEngDic_Guide.this, AllEngDic_Setting.class);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(i);
							finish();
						}
					}
				});
			}

			((ViewPager)pager).addView(v, 0);
			return v; 
		}

		//�� ��ü ����.
		@Override public void destroyItem(View pager, int position, Object view) {
			((ViewPager)pager).removeView((View)view);
		}

		// instantiateItem�޼ҵ忡�� ������ ��ü�� �̿��� ������
		@Override public boolean isViewFromObject(View view, Object obj) { return view == obj; }

		@Override public void finishUpdate(View arg0) {}
		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override public Parcelable saveState() { return null; }
		@Override public void startUpdate(View arg0) {}
	}
}
