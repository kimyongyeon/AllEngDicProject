package com.all.eng.dic.kyy.project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AllEngDic_Request extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allengdic_request);

		ViewPager pager = (ViewPager) findViewById(R.id.pager_request_pager);
		pager.setAdapter(new PagerAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 6;
			}

			@Override
			public Object instantiateItem(View pager, int position) {
				// TODO Auto-generated method stub

				ImageView imgView = new ImageView(AllEngDic_Request.this);
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				imgView.setAdjustViewBounds(true);
				imgView.setLayoutParams(params);
				switch (position) {
				case 0:
					imgView.setImageResource(R.drawable.port_01);
					break;
				case 1:
					imgView.setImageResource(R.drawable.port_02);
					break;
				case 2:
					imgView.setImageResource(R.drawable.port_03);
					break;
				case 3:
					imgView.setImageResource(R.drawable.port_04);
					break;
				case 4:
					imgView.setImageResource(R.drawable.port_05);
					break;
				case 5:
					imgView.setImageResource(R.drawable.port_06);
					break;
				}
				((ViewPager) pager).addView(imgView, 0);
				return imgView;
			}

			@Override
			public void destroyItem(View pager, int position, Object view) {
				// TODO Auto-generated method stub

				((ViewPager) pager).removeView((View) view);

			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}
		});

		findViewById(R.id.layout_request_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		findViewById(R.id.btn_request_request).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] TO = { "gangs@broogs.com" };
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setData(Uri.parse("mailto:"));
				emailIntent.setType("message/rfc822");

				emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

				try {
					startActivity(Intent.createChooser(emailIntent, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(AllEngDic_Request.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
