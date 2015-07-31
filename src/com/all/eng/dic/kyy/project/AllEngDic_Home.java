package com.all.eng.dic.kyy.project;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyAdViewListener;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;
import com.google.ads.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import kr.co.withmob.withmobsdk_v01.WithMob;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class AllEngDic_Home extends Activity implements OnClickListener,
		CaulyAdViewListener, CaulyInterstitialAdListener {
	public static boolean m_HelpPageFlag = true;
	Button bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt10;
	ImageButton bt1, bt2, inputbox_icon, fav_add_icon, home_ad, home_del_btn2,
			home_search_btn;
	LinearLayout ll, ll2;
	// HorizontalScrollView hsv;
	ListView lv;
	AutoCompleteTextView et1;
	WebView wv;
	private WebSettings ws;
	private String m_Url;
	private AllEngDic_dbHandler db;
	// com.cauly.android.ad.AdView ad;
	PopupWindow popup;
	private boolean wordSaveFlag = false;
	private String wordList[];

	private final int COUNT = 2;
	private ViewPager mPager; // �� ������

	private final String btName1 = "���̹�";
	private final String btName2 = "����";
	private final String btName3 = "WIKIPEDIA";

	// ���� ��û�� ���� App Code
	private static final String APP_CODE = "i3PXxt6Osh";
	private CaulyAdView javaAdView;
	private CaulyInterstitialAd interstitialAd;

	private OnClickListener mFavoliteClick = new OnClickListener() { // Ŭ�� �̺�Ʈ
																		// ��ü
		public void onClick(View v) {
			// Toast.makeText(getApplication(), "mFavoliteClick...", 0).show();
			// wordSave();

			// InputMethodManager imm = (InputMethodManager)
			// getSystemService(Context.INPUT_METHOD_SERVICE);
			// imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);
			//
			// popup_list(); // ���ã�� �˾��� ���.
			// fav_add_icon.setVisibility(View.GONE);

			Intent i = new Intent(getApplication(), AllEngDic_Word.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_right_out);
			finish();
		}
	};

	private OnClickListener mSettingClick = new OnClickListener() { // Ŭ�� �̺�Ʈ ��ü
		public void onClick(View v) {
			// Toast.makeText(getApplication(), "mSettingClick...", 0).show();

			Intent i = new Intent(getApplication(), AllEngDic_Setting.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			finish();
		}
	};

	// ���ۼ��� ���� : 2012.07.20
	// ����� ���� api
	// http://www.google.com/dictionary/json?callback=maesiltea&sl=en&tl=ko&q=
	private String arryAddr[] = {
			"http://m.endic.naver.com/search.nhn?isOnlyViewEE=N&query=", // ���̹�
			"http://ko.wiktionary.org/wiki/", // ��Ű
			"http://m.engdic.daum.net/dicen/mobile_search.do?q=" // ����
	};
	DTO_Address addr = new DTO_Address();

	// ��ư�� ����� �̸� ���� �� �������(���� 1ȸ��)
	private boolean initPotalSetting() {
		addr = new DTO_Address();
		db = new AllEngDic_dbHandler(this);

		if (db == null) {
			Toast.makeText(this, "�������� DB�۾��� �� �� �����ϴ�.", 0).show();
			return false;
		}

		addr.setIdx("1");
		addr.setAddr("");
		addr.setName(btName1);
		addr.setChk(true);
		db.dataPotalSave(addr);

		addr = new DTO_Address();
		addr.setIdx("2");
		addr.setAddr("");
		addr.setName(btName2);
		addr.setChk(true);
		db.dataPotalSave(addr);

		addr = new DTO_Address();
		addr.setIdx("3");
		addr.setAddr("");
		addr.setName(btName3);
		addr.setChk(true);
		db.dataPotalSave(addr);

		return true;
	}

	// ������ư ����
	Button bt[];
	private int currIndex;
	private int currTotalCount;
	private boolean currFlag = false;

	private void initBt() {
		currTotalCount = 0;

		ll = (LinearLayout) findViewById(R.id.home_Ll);
		ll.removeAllViews();

		Vector<DTO_Address> vec = new Vector<DTO_Address>();
		DTO_Address a = new DTO_Address();
		vec = db.dataPotalRead();
		LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		buttonParams.weight = 1;
		bt = new Button[vec.size()];
		for (int i = 0; i < bt.length; i++) {
			a = new DTO_Address();
			a = vec.get(i);

			if (a.isChk()) {

				currTotalCount++;

				bt[i] = new Button(this);
				// bt[i].setText(a.getName());
				bt[i].setLayoutParams(buttonParams);
				bt[i].setTag(a.getName());
				if (a.getName().equals(btName1)) {
					bt[i].setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bottom_naver_btn));
					ll.addView(bt[i]);

					if (currFlag == false) {
						currIndex = i;
						currFlag = true;
					}
				}
				if (a.getName().equals(btName2)) {
					bt[i].setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bottom_daum_btn));
					ll.addView(bt[i]);

					if (currFlag == false) {
						currIndex = i;
						currFlag = true;
					}
				}
				if (a.getName().equals(btName3)) {
					bt[i].setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bottom_google_btn));
					ll.addView(bt[i]);

					if (currFlag == false) {
						currIndex = i;
						currFlag = true;
					}
				}

				bt[i].setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Button btTemp = (Button) v;

						buttonInit(bt, currTotalCount);

						Log.d("kimyongyeon", currTotalCount + "");
						Log.d("kimyongyeon", bt.length + "");

						if (btTemp.getTag().toString().equals(btName1)) { // ���̹�
							btTemp.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.bottom_naver_pre));
							webSetting();
							m_Url = arryAddr[0]
									+ et1.getText().toString().trim();
							wv.loadUrl(m_Url);
							// ad.setVisibility(View.INVISIBLE);
							// home_ad.setVisibility(View.VISIBLE);
						}
						if (btTemp.getTag().toString().equals(btName3)) { // ��Ű
							btTemp.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.bottom_wiki_pre));
							webSetting();
							m_Url = arryAddr[1]
									+ et1.getText().toString().trim();
							Log.d("kimyongyeon", m_Url);
							wv.loadUrl(m_Url);
							// ad.setVisibility(View.VISIBLE);
							// home_ad.setVisibility(View.GONE);
						}
						if (btTemp.getTag().toString().equals(btName2)) { // ����
							btTemp.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.bottom_daum_pre));
							webSetting();
							m_Url = arryAddr[2]
									+ et1.getText().toString().trim()
									+ "#1&keyword="
									+ et1.getText().toString().trim();
							wv.loadUrl(m_Url);
							// ad.setVisibility(View.INVISIBLE);
							// home_ad.setVisibility(View.VISIBLE);
						}
					}
				});
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		boolean chk = new Internet_Check(this).internetChk();
		if (!chk) {
			Toast.makeText(this, "���ͳ��� ����� �� �����ϴ�.", 0).show();
		}

		initBt();

		bt[currIndex].setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bottom_naver_pre));
	}

	// �޴� ��ư�� ó�� 1ȸ ������ �� ȣ��
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem mi1 = menu.add(0, 0, Menu.NONE, "��λ���");
		return true;
	}

	// �޴���ư Ŭ���̺�Ʈ ó��
	public boolean onOptionsItemSelected(MenuItem item) {
		int gId = item.getGroupId();
		if (gId == 0) { // ��λ���
			delDialog();
		}
		return false;

	}

	private void delDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.guitar);
		builder.setTitle("����");
		builder.setMessage("������ ��� �����Ͻðڽ��ϱ�? \n (������ ���� ���� �ʽ��ϴ�!)");
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// showMsg("which : "+ which);
				if (which == -1) {
					db = new AllEngDic_dbHandler(AllEngDic_Home.this);
					db.dropT2();
					dataList();
				} else {
				}
			}
		};

		builder.setPositiveButton("��", listener);
		builder.setNegativeButton("�ƴϿ�", listener);
		builder.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (db != null)
			db.dbClose();

		AllEngDic_TabHost_Main.tabFlag = false;
	}

	private void getStrData() {
		Intent i = getIntent();
		if (i != null) {
			String str = i.getStringExtra("AllEngDic_TabHost_Main");
			webSetting();
			m_Url = "http://m.endic.naver.com/search.nhn?isOnlyViewEE=N&query="
					+ str;
			wv.loadUrl(m_Url);
			// buttonInit();
			// bt3.setBackgroundResource(R.drawable.botbuttonoff);
			et1.setText(str.trim());
		}
	}

	private boolean keyFlag = false;
	public static boolean tabFlag = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allengdic_home);

		// ad = (com.cauly.android.ad.AdView) findViewById(R.id.ad);
		// ad.setVisibility(View.INVISIBLE);
		bt1 = (ImageButton) findViewById(R.id.home_Bt01);
		bt2 = (ImageButton) findViewById(R.id.home_Bt02);
		inputbox_icon = (ImageButton) findViewById(R.id.home_inputbox_icon);
		fav_add_icon = (ImageButton) findViewById(R.id.home_fav_add_icon);
		// home_ad = (ImageButton) findViewById(R.id.home_ad);
		home_del_btn2 = (ImageButton) findViewById(R.id.home_del_btn2);
		home_search_btn = (ImageButton) findViewById(R.id.home_search_btn);
		// hsv = (HorizontalScrollView) findViewById(R.id.home_Hsv);
		fav_add_icon.setVisibility(View.GONE);
		et1 = (AutoCompleteTextView) findViewById(R.id.home_Edit01);
		db = new AllEngDic_dbHandler(this);
		Vector<DTO_Word> vec = db.dataSearchRead();
		wordList = new String[vec.size()];
		for (int i = 0; i < vec.size(); i++) {
			DTO_Word d = vec.get(i);
			wordList[i] = d.getWord();
		}
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_dropdown_item_1line, wordList);
		et1.setAdapter(adapter);

		wv = (WebView) findViewById(R.id.home_Web01);
		// home_ad.setVisibility(View.GONE);
		//
		// home_ad.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// WithMob withMob = new WithMob();
		// withMob.openWithMob(AllEngDic_Home.this, R.layout.withmob_adv,
		// R.id.withmob_adv_view, R.string.withmob_uid);
		// }
		// });
		// Search Button Click
		home_search_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);

				lv.setVisibility(View.GONE);
				wv.setVisibility(View.VISIBLE);
				// hsv.setVisibility(View.VISIBLE);
				ll.setVisibility(View.VISIBLE);
				// ad.setVisibility(View.GONE);
				// home_ad.setVisibility(View.VISIBLE);
				webSetting();
				m_Url = "http://m.endic.naver.com/search.nhn?isOnlyViewEE=N&query="
						+ et1.getText().toString().trim();
				wv.loadUrl(m_Url);
				if (et1.getText().toString().length() > 0)
					searchWordSave();
				bt[currIndex].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bottom_naver_pre));

				keyFlag = true;
			}
		});

		home_del_btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et1.setText("");
				home_del_btn2.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.del_btn3));
				// button gone
				// hsv.setVisibility(View.GONE);
				ll.setVisibility(View.GONE);
				// ad.setVisibility(View.VISIBLE);
				// home_ad.setVisibility(View.GONE);

				db = new AllEngDic_dbHandler(AllEngDic_Home.this);
				Vector<DTO_Word> vec = db.dataSearchRead();
				wordList = new String[vec.size()];
				for (int i = 0; i < vec.size(); i++) {
					DTO_Word d = vec.get(i);
					wordList[i] = d.getWord();
				}
				ArrayAdapter adapter = new ArrayAdapter(AllEngDic_Home.this,
						android.R.layout.simple_dropdown_item_1line, wordList);
				et1.setAdapter(adapter);
			}
		});

		et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					return true;
				} else {
					lv.setVisibility(View.GONE);
					wv.setVisibility(View.VISIBLE);
					// hsv.setVisibility(View.VISIBLE);
					ll.setVisibility(View.VISIBLE);
					// ad.setVisibility(View.GONE);
					// home_ad.setVisibility(View.VISIBLE);
					webSetting();
					m_Url = "http://m.endic.naver.com/search.nhn?isOnlyViewEE=N&query="
							+ et1.getText().toString().trim();
					wv.loadUrl(m_Url);
					if (et1.getText().toString().length() > 0)
						searchWordSave();
					bt[currIndex].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.bottom_naver_pre));

					keyFlag = true;

					return false;
				}
			}
		});

		bt1.setOnClickListener(mSettingClick);
		bt2.setOnClickListener(mFavoliteClick);

		boolean isSettings = initPotalSetting();
		if (isSettings) {
			initBt();
		} else {
			Toast.makeText(getApplication(),
					"�������� DB�۾��� �̷������ �ʾҽ��ϴ�.\n" + "�Ϻα���� �������� �� �ֽ��ϴ�.", 0)
					.show();
		}

		if (et1.getText().toString().equals("")) {
			wv.setVisibility(View.GONE);
			lv = (ListView) findViewById(R.id.home_WordList);
			lv.setVisibility(View.VISIBLE);
			// hsv.setVisibility(View.GONE);
			ll.setVisibility(View.GONE);
			dataList();
		}

		et1.addTextChangedListener(mWatcher);// ����Ʈ�ؽ�Ʈ�� �����ʸ� �����

		inputbox_icon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (wordSaveFlag == true) {
					wordSave();
					fav_add_icon.setVisibility(View.VISIBLE);
				}
			}
		});

		// et1.setInputType(0);

		et1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (keyFlag == false) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(et1, 0);
				} else {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);

					keyFlag = false;
				}
			}
		});

		String str = "";
		Intent i = getIntent();
		if (tabFlag == true) {
			if (i != null) {

				bt[currIndex].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bottom_naver_pre));

				str = i.getStringExtra("AllEngDic_Word");
				et1.setText(str);

				lv.setVisibility(View.GONE);
				wv.setVisibility(View.VISIBLE);
				// hsv.setVisibility(View.VISIBLE);
				ll.setVisibility(View.VISIBLE);
				// ad.setVisibility(View.GONE);
				// home_ad.setVisibility(View.VISIBLE);

				webSetting();
				m_Url = "http://m.endic.naver.com/search.nhn?isOnlyViewEE=N&query="
						+ et1.getText().toString().trim();
				wv.loadUrl(m_Url);

				tabFlag = false;
			}
		}

		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);

		// ////////////////////
		//
		// Java ��� ���� ����
		//
		// ////////////////////

		// CaulyAdInfo ����
		// ��� ����� �����ϰ� ���� ��û�� ������ �� �ִ�.
		CaulyAdInfo interstitialAdInfo = new CaulyAdInfoBuilder(APP_CODE)
				.build();

		// ���� ���� ����
		interstitialAd = new CaulyInterstitialAd();
		interstitialAd.setAdInfo(interstitialAdInfo);
		interstitialAd.setInterstialAdListener(this);

		// ���鱤�� ���� �� back ��ư�� ���⸦ ���� ��� disableBackKey();�� �߰��Ѵ�
		// ��, requestInterstitialAd ������ �߰��Ǿ�� �մϴ�.
		// interstitialAd.disableBackKey();

		// ���� ��û. ���� ������ CaulyInterstitialAdListener�� onReceiveInterstitialAd����
		// ó���Ѵ�.
		interstitialAd.requestInterstitialAd(this);

	}

	// private void popup_list() {
	// View v = View.inflate(this, R.layout.popup_list, null);
	// popup = new PopupWindow(v, LayoutParams.FILL_PARENT, 500, true);
	// popup.setOutsideTouchable(true); // �̺κ��� �������־�� �˾��� ������ �ٸ��κп� �̺�Ʈ��
	// // �ټ��ֽ��ϴ�.
	// popup.setBackgroundDrawable(new BitmapDrawable()); // �̺κп� �̺�Ʈ�� �����Ե˴ϴ�.
	// popup.showAtLocation(v, Gravity.CENTER | Gravity.BOTTOM, 100, 193);
	//
	// lv = (ListView) v.findViewById(R.id.wordList);
	// dataList2();
	// }

	TextWatcher mWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

			if (et1.getText().length() <= 0) {
				wv.setVisibility(View.GONE);
				lv = (ListView) findViewById(R.id.home_WordList);
				lv.setVisibility(View.VISIBLE);
				dataList();
				inputbox_icon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.inputbox_icon));
				wordSaveFlag = false;

				et1.setTextSize(13);

			} else {

				home_del_btn2.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.del_btn2));

				home_search_btn.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.search_btn));

				if (et1.getText().length() > 22) {
					et1.setTextSize(16);
				} else {
					et1.setTextSize(18);
				}

				inputbox_icon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.favorite_btn));
				wordSaveFlag = true;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	// Pager �ƴ��� ����
	public void dataList() {
		Vector<DTO_Word> vec = null;

		try {
			db = new AllEngDic_dbHandler(this);
			// vec = db.dataRead();
			vec = db.dataSearchRead();
			final WordAdapter sa = new WordAdapter(this, vec);

			lv.setAdapter(sa);

			// lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//
			// public void onItemClick(AdapterView<?> arg0, View arg1,
			// int arg2, long arg3) {
			// DTO_Word w = new DTO_Word();
			// w = sa.getItem(arg2);
			// String str = w.getWord();
			// Intent i = new Intent(AllEngDic_Home.this,
			// AllEngDic_Home.class);
			// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			// | Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.putExtra("AllEngDic_Word", str);
			// startActivity(i);
			// }
			//
			// });
		} catch (Exception e) {
			// showMsg("����� ���� �����ϴ�.");
		}
	}

	// public void dataList2() {
	// Vector<DTO_Word> vec = null;
	//
	// try {
	// db = new AllEngDic_dbHandler(this);
	// vec = db.dataRead();
	// final Popup_WordAdapter sa = new Popup_WordAdapter(this, vec);
	//
	// lv.setAdapter(sa);
	//
	// lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	//
	// public void onItemClick(AdapterView<?> arg0, View arg1,
	// int arg2, long arg3) {
	// DTO_Word w = new DTO_Word();
	// w = sa.getItem(arg2);
	// String str = w.getWord();
	// Intent i = new Intent(AllEngDic_Home.this,
	// AllEngDic_Home.class);
	// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// | Intent.FLAG_ACTIVITY_NEW_TASK);
	// i.putExtra("AllEngDic_Word", str);
	// startActivity(i);
	// }
	//
	// });
	// } catch (Exception e) {
	// // showMsg("����� ���� �����ϴ�.");
	// }
	// }

	private Map<Integer, Boolean> check = new HashMap<Integer, Boolean>();

	class WordAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private DTO_Word w;
		private Vector<DTO_Word> vec = new Vector<DTO_Word>();

		WordAdapter(Context context, Vector<DTO_Word> vec) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.vec = vec;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return vec.size();
		}

		public DTO_Word getItem(int position) {
			// TODO Auto-generated method stub
			return vec.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		TextView tv_str1;
		ImageButton ib;
		LinearLayout btnLayout;
		RelativeLayout tv_str1Layout;

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.allengdic_word_row_list, parent, false);
			}

			tv_str1 = (TextView) convertView.findViewById(R.id.rowTx01); // �ܾ�
			tv_str1Layout = (RelativeLayout) convertView
					.findViewById(R.id.rowTx01Layout);// �ܾ����ִ� ���̾ƿ�
			TextView tv_str2 = (TextView) convertView
					.findViewById(R.id.rowTx02); // ��¥
			ib = (ImageButton) convertView.findViewById(R.id.rowBt01);
			btnLayout = (LinearLayout) convertView
					.findViewById(R.id.rowBt01Layout);

			w = new DTO_Word();
			w = vec.get(position);
			// tv_str1.setTag(position);
			tv_str1Layout.setTag(position);
			// ib.setTag(position);
			btnLayout.setTag(position);
			tv_str1.setText(w.getWord()); // �ܾ�
			tv_str2.setText(w.getDate()); // ��¥

			if (w.isChk()) {
				ib.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.list_favorite_btn_pre));
			} else {
				ib.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.list_favorite_btn));
			}

			btnLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// ���ã�� ��ư ����
					ImageButton btTemp = ((ImageButton) ((ViewGroup) v)
							.getChildAt(0));
					// ImageButton btTemp = (ImageButton) v;
					btTemp.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.list_favorite_btn_pre));

					DTO_Word word = new DTO_Word();
					word = vec.get((Integer) v.getTag());
					Log.d("Layout Tag", v.getTag().toString());

					if (word.isChk()) {
						// word.setChk(false);
						// db.dataSearchUpdate(word);
						// ib.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_favorite_btn));
						//
						// word = vec.get((Integer)v.getTag());
						// db.dataDelete(word.getIdx()+"");

					} else {
						word.setChk(true);
						db.dataSearchUpdate(word);

						// ���ã�⿡ �߰�.
						Date date = new Date();
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss a");
						String str = format.format(date);

						DTO_Word w = new DTO_Word();
						w.setWord(word.getWord());
						w.setDate(str);
						w.setChk(true);

						db.dataSave(w);
						fav_add_icon.setVisibility(View.VISIBLE);

						Toast.makeText(getApplication(),
								"'" + word.getWord() + "' �ܾ �߰��Ǿ����ϴ�.",
								Toast.LENGTH_SHORT).show();
					}

					// dataList();
					// notifyDataSetChanged();
				}
			});

			tv_str1Layout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DTO_Word w = new DTO_Word();
					int vv = (Integer) v.getTag();
					w = vec.get(vv);
					String str = w.getWord();
					if (!str.equals("")) {

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);

						et1.setText(str);
						initBt();
						lv.setVisibility(View.GONE);
						wv.setVisibility(View.VISIBLE);
						// hsv.setVisibility(View.VISIBLE);
						ll.setVisibility(View.VISIBLE);
						// ad.setVisibility(View.GONE);
						// home_ad.setVisibility(View.VISIBLE);

						webSetting();
						m_Url = "http://m.endic.naver.com/search.nhn?isOnlyViewEE=N&query="
								+ et1.getText().toString().trim();
						wv.loadUrl(m_Url);

						bt[currIndex].setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.bottom_naver_pre));

						keyFlag = true;

					} else {
						Toast.makeText(getParent(), "��Ȯ�ϰ� ��ġ�� �ֽñ� �ٶ��ϴ�.", 0)
								.show();
					}
				}
			});

			return convertView;
		}
	}

	// class Popup_WordAdapter extends BaseAdapter {
	// private LayoutInflater inflater;
	// private DTO_Word w;
	// private Vector<DTO_Word> vec = new Vector<DTO_Word>();
	//
	// Popup_WordAdapter(Context context, Vector<DTO_Word> vec) {
	// inflater = (LayoutInflater) context
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// this.vec = vec;
	// }
	//
	// public int getCount() {
	// // TODO Auto-generated method stub
	// return vec.size();
	// }
	//
	// public DTO_Word getItem(int position) {
	// // TODO Auto-generated method stub
	// return vec.get(position);
	// }
	//
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return position;
	// }
	//
	// TextView tv_str1;
	//
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // TODO Auto-generated method stub
	// if (convertView == null) {
	// convertView = inflater.inflate(
	// R.layout.allengdic_word_row_list2, parent, false);
	// }
	//
	// tv_str1 = (TextView) convertView.findViewById(R.id.row2Tx01); // �ܾ�
	// TextView tv_str2 = (TextView) convertView
	// .findViewById(R.id.row2Tx02); // ��¥
	// // CheckBox checkbox = (CheckBox)
	// // convertView.findViewById(R.id.row_select_checkbox); // ���� üũ�ڽ�
	//
	// tv_str1.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// DTO_Word w = new DTO_Word();
	// w = vec.get((Integer) v.getTag());
	// String str = w.getWord();
	// if (!str.equals("")) {
	// initBt();
	//
	// et1.setText(str);
	//
	// lv.setVisibility(View.GONE);
	// wv.setVisibility(View.VISIBLE);
	// hsv.setVisibility(View.VISIBLE);
	// ad.setVisibility(View.GONE);
	// home_ad.setVisibility(View.VISIBLE);
	//
	// webSetting();
	// m_Url = "http://m.endic.naver.com/search.nhn?isOnlyViewEE=N&query="
	// + et1.getText().toString().trim();
	// wv.loadUrl(m_Url);
	//
	// bt[currIndex].setBackgroundDrawable(getResources()
	// .getDrawable(R.drawable.bottom_naver_pre));
	//
	// keyFlag = true;
	//
	// } else {
	// Toast.makeText(getParent(), "��Ȯ�ϰ� ��ġ�� �ֽñ� �ٶ��ϴ�.", 0)
	// .show();
	// }
	// }
	// });
	//
	// w = new DTO_Word();
	// w = vec.get(position);
	// // checkbox.setTag(position);
	// tv_str1.setTag(position);
	// tv_str1.setText(w.getWord()); // �ܾ�
	// tv_str2.setText(w.getDate()); // ��¥
	//
	// return convertView;
	// }
	// }

	private void webSetting() {
		ws = wv.getSettings();
		// Zoom�� �������� �����մϴ�. ws.setSupportZoom(true); // WebView�� �����
		// zoom mechanism�� ���ǰ� ������ treu�� ���� �ݴϴ�.
		ws.setBuiltInZoomControls(true);
		ws.setDefaultZoom(ZoomDensity.FAR);
		ws.setUseWideViewPort(true);
		// wv.setBackgroundColor(Color.BLACK);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setHorizontalScrollBarEnabled(false); //
		wv.setWebViewClient(new WebViewClient()); // �߿�
	}

	private String m_str;

	private void buttonInit(Button[] bt, int currTotalCount) {

		for (int i = 0; i < bt.length; i++) {

			if (bt[i] != null) {
				if (bt[i].getTag().toString().equals(btName2)) {
					bt[i].setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bottom_daum_btn));
				}
				if (bt[i].getTag().toString().equals(btName1)) {
					bt[i].setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bottom_naver_btn));
				}
				if (bt[i].getTag().toString().equals(btName3)) {
					bt[i].setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bottom_google_btn));
				}
			}
		}
	}

	private void wordSave() {
		// ���ó�¥
		Date date = new Date();
		// SimpleDateFormat format = new SimpleDateFormat(
		// "yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		String str = format.format(date);

		DTO_Word w = new DTO_Word();
		String word = et1.getText().toString().trim();
		w.setWord(word);
		w.setDate(str);
		w.setChk(false);

		// db = new AllEngDic_dbHandler(this);
		db.dataSave(w);
		showMsg();
	}

	private void searchWordSave() {
		// ���ó�¥
		Date date = new Date();
		// SimpleDateFormat format = new SimpleDateFormat(
		// "yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = format.format(date);

		DTO_Word w = new DTO_Word();
		String word = et1.getText().toString().trim();
		w.setWord(word);
		w.setDate(str);
		w.setChk(false);

		// db = new AllEngDic_dbHandler(this);
		db.dataSearchSave(w);
		// showMsg();
	}

	private void showMsg() {
		String str = et1.getText().toString().trim();
		Toast.makeText(this, "'" + str + "' �ܾ �߰��Ǿ����ϴ�.", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.home_Bt01:
			// ����
		case R.id.home_Bt02:
			// ���ã��
			// wordSave();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		endDialog();
	}

	private void endDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.guitar);
		builder.setTitle("����");
		builder.setMessage("�����Ͻðڽ��ϱ�?");
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// showMsg("which : "+ which);
				if (which == -1) {
					finish();
				} else {
				}
			}
		};

		builder.setPositiveButton("��", listener);
		builder.setNegativeButton("�ƴϿ�", listener);
		builder.show();
	}

	// ////////////////////////////
	//
	// Java ��� ��� ���� Listener
	//
	// ////////////////////////////

	// CaulyAdViewListener
	// ���� ���ۿ� ���� ���� ó���� �ʿ� ���� ���,
	// Activity�� "implements CaulyAdViewListener" �κ� �����ϰ� ���� ����.

	@Override
	public void onReceiveAd(CaulyAdView adView, boolean isChargeableAd) {
		// ���� ���� ���� & ����� ��� ȣ���.
		// ���ŵ� ���� ���� ������ ��� isChargeableAd ���� false ��.
		if (isChargeableAd == false) {
			Log.d("CaulyExample", "free banner AD received.");
		} else {
			Log.d("CaulyExample", "normal banner AD received.");
		}
	}

	@Override
	public void onFailedToReceiveAd(CaulyAdView adView, int errorCode,
			String errorMsg) {
		// ��� ���� ���� ������ ��� ȣ���.
		Log.d("CaulyExample", "failed to receive banner AD." + errorCode + " "
				+ errorMsg);
	}

	@Override
	public void onShowLandingScreen(CaulyAdView adView) {
		// ���� ��ʸ� Ŭ���Ͽ� ���� �������� ���� ��� ȣ���.
		Log.d("CaulyExample", "banner AD landing screen opened.");
	}

	@Override
	public void onCloseLandingScreen(CaulyAdView adView) {
		// ���� ��ʸ� Ŭ���Ͽ� ���� ���� �������� ���� ��� ȣ���.
		Log.d("CaulyExample", "banner AD landing screen closed.");
	}

	// Activity ��ư ó��
	// - Java ��� ���� ���� ��ư
	public void onReloadJavaAdView(View button) {
		javaAdView.reload();
	}

	// ////////////////////////////
	//
	// Java ��� ���� ���� Listener
	//
	// ////////////////////////////

	// CaulyInterstitialAdListener
	// ���� ������ ���, ���� ���� �� �ڵ����� ������� �����Ƿ�,
	// �ݵ�� onReceiveInterstitialAd �޼ҵ忡�� ���� ó���� �־�� �Ѵ�.

	@Override
	public void onReceiveInterstitialAd(CaulyInterstitialAd ad,
			boolean isChargeableAd) {
		// ���� ���� ������ ��� ȣ���.
		// ���ŵ� ���� ���� ������ ��� isChargeableAd ���� false ��.
		if (isChargeableAd == false) {
			Log.d("CaulyExample", "free interstitial AD received.");
		} else {
			Log.d("CaulyExample", "normal interstitial AD received.");
		}

		// ���� ����
		ad.show();
	}

	@Override
	public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd ad,
			int errorCode, String errorMsg) {
		// ���� ���� ���� ������ ��� ȣ���.
		Log.d("CaulyExample", "failed to receive interstitial AD.");
	}

	@Override
	public void onClosedInterstitialAd(CaulyInterstitialAd ad) {
		// ���� ���� ���� ��� ȣ���.
		Log.d("CaulyExample", "interstitial AD closed.");
	}

	@Override
	public void onLeaveInterstitialAd(CaulyInterstitialAd arg0) {
		// TODO Auto-generated method stub

		interstitialAd.cancel();
	}
}
