package com.all.eng.dic.kyy.project;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AllEngDic_Word extends Activity {
	public ListView lv;
	public AllEngDic_dbHandler db;
	/**
	 * 정렬로직 추가
	 * 사용방법 : mAdapter.sort(new ItemInfoSort());   //Adapter의 sort 함수 이용.
	 */
	/*private static final Collator sCollator = Collator.getInstance();  //Collator 객체 생성
	//Adapter에 들어가는 List에 대한 자료형 으로 정렬 ( a, b의 순서를 바꾸면 역정렬 )
	static class ItemInfoSort implements Comparator<String> {            
        public final int compare(String a, String b) {
            return sCollator.compare(a, b);  
        }
	}*/
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(db != null)
			db.dbClose();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		boolean chk = new Internet_Check(this).internetChk();
		if(!chk){
			Toast.makeText(this, "인터넷을 사용할 수 없습니다.", 0).show();
		}
		dataList();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allengdic_word);

		lv = (ListView) findViewById(R.id.wordList);
		ImageButton bt = (ImageButton) findViewById(R.id.wordBack);
		Button del_bt = (Button) findViewById(R.id.word_del);
		Button send_bt = (Button) findViewById(R.id.word_send);
		
		del_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delDialog();
			}
		});

		send_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = "";
				Vector<DTO_Word> vec = null;
				vec = db.dataRead();
				DTO_Word w = new DTO_Word();
				for(int i=0; i<vec.size(); i++){
					w = vec.get(i);
					str += w.getWord() + ",";
				}
				sms_textSend(str);
			}
		});
		
		
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplication(), AllEngDic_Home.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				//overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
				finish();
			}
		});
		dataList();
	}

	public void dataList() {
		Vector<DTO_Word> vec = null;

		try {
			db = new AllEngDic_dbHandler(this);
			vec = db.dataRead();
			final WordAdapter sa = new WordAdapter(this, vec);

			lv.setAdapter(sa);

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					DTO_Word w = new DTO_Word();
					w = sa.getItem(arg2);
					String str = w.getWord();
					Intent i = new Intent(AllEngDic_Word.this, AllEngDic_Home.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("AllEngDic_Word", str);
					startActivity(i);
					finish();
				}

			});
		} catch (Exception e) {
			//showMsg("저장된 값이 없습니다.");
		}
	}
	private Map<Integer, Boolean> check = new HashMap<Integer, Boolean>();
	//private ArrayList<Integer> m_Pos = new ArrayList<Integer>();
	//private int m_Position;
	//CheckBox chk;

	class WordAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private DTO_Word w;
		private Vector<DTO_Word> vec = new Vector<DTO_Word>();

		WordAdapter(Context context, Vector<DTO_Word> vec) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.allengdic_word_row_list2, parent, false);
			}
			
			tv_str1 = (TextView) convertView.findViewById(R.id.row2Tx01); // 단어
			TextView tv_str2 = (TextView) convertView.findViewById(R.id.row2Tx02); // 날짜
			ib = (ImageButton) convertView.findViewById(R.id.row2Bt01);
			//CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.row_select_checkbox); // 지움 체크박스
			
			ib.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					w = vec.get((Integer)v.getTag());
					db.dataDelete(w.getIdx()+"");
					dataList();
//					Animation animation = new AlphaAnimation( 0.0f, 1.0f ); // 투명도를 조절. 페이드아웃
//					tv_str1.setAnimation(animation);					
					notifyDataSetChanged();
					
				}
			});
			
			tv_str1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					AllEngDic_Home.tabFlag = true;
					
					DTO_Word w = new DTO_Word();
					w = vec.get((Integer)v.getTag());
					String str = w.getWord();
					if(!str.equals("")){
						Intent i = new Intent(AllEngDic_Word.this, AllEngDic_Home.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						i.putExtra("AllEngDic_Word", str);
						startActivity(i);
						finish();
					}else{
						Toast.makeText(getParent(), "정확하게 터치해 주시기 바랍니다.", 0).show();
					}
				}
			});

			w = new DTO_Word();
			w = vec.get(position);
			//checkbox.setTag(position);
			ib.setTag(position);
			tv_str1.setTag(position);
			tv_str1.setText(w.getWord()); // 단어
			tv_str2.setText(w.getDate()); // 날짜

			return convertView;
		}
	}
	
	public void onCheckboxClicked(View v){
		// 체크했을때...
		if(((CheckBox)v).isChecked()){
			check.put((Integer)v.getTag(), true);
			//Log.d("AllEngDic",  v.getTag()+"");
		}else{
			check.put((Integer)v.getTag(), false);
			//Log.d("AllEngDic",  v.getTag()+"");
		}
	}

	private void showMsg(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	// 메뉴 버튼을 처음 1회 눌렀을 때 호출
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem mi1 = menu.add(0, 0, Menu.NONE, "백업하기");
		// mi1.setIcon(R.drawable.setting);
		MenuItem mi2 = menu.add(1, 0, Menu.NONE, "복구하기");
		// mi2.setIcon(R.drawable.setting);

		return true;
	}

	private void sms_textSend(String name) {
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_SUBJECT, "내 단어장 내보내기");
		it.putExtra(Intent.EXTRA_TEXT, name);
		it.setType("text/plain");
		// Intent_Manager.a.startActivity(it);
		startActivity(Intent.createChooser(it,
				"How do you want to send message?"));
	}

	// 메뉴버튼 클릭이벤트 처리
	public boolean onOptionsItemSelected(MenuItem item) {
		int gId = item.getGroupId();
		if (gId == 0) { // 백업하기
			String str = "";
			Vector<DTO_Word> vec = null;
			db = new AllEngDic_dbHandler(this);
			vec = db.dataRead();
			
			String[] word = new String[vec.size()];
			String[] date = new String[vec.size()];
			
			TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//			com_device = tm.getDeviceId();
//			com_phoneNumber = tm.getLine1Number();
			
			String phone = "";
			phone = tm.getLine1Number();
			
			DTO_Word w = new DTO_Word();
			for(int i=0; i<vec.size(); i++){
				w = vec.get(i);
				word[i] = w.getWord();
				date[i] = w.getDate();
			}
			Util u = new Util();
			String result = u.postBackup(word, date, phone);
			if(result.equals("1")){
				Toast.makeText(this, "정상적으로 백업 되었습니다.", 0).show();
			}
		}
		if (gId == 1) { // 복구하기
			delRollDialog();
		}
//		if (gId == 2) { // 삭제
//			delDialog2();
//		}
		return false;

	}
	
	private void rollBackProcess(){
		db = new AllEngDic_dbHandler(AllEngDic_Word.this);
		db.dropT();
		dataList();
		// 서버 데이터를 불러온다.
		Util u = new Util();
		String phone = "";
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		phone = tm.getLine1Number();
		vec = u.postRollBack(phone);
		wordSave(vec);
		dataList();
	}
	
	private void wordSave(Vector<DTO_Word> vec) {
		DTO_Word w = new DTO_Word();
		
		for(int i=0; i<vec.size(); i++){
			String word = vec.get(i).getWord();
			w.setWord(word);
			w.setDate(vec.get(i).getDate());
			w.setChk(false);
			db.dataSave(w);
		}
	}
	
	
//	public void dataServerList(Vector vec) {
//		try {
//			final WordAdapter sa = new WordAdapter(this, vec);
//
//			lv.setAdapter(sa);
//
////			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////
////				public void onItemClick(AdapterView<?> arg0, View arg1,
////						int arg2, long arg3) {
////					DTO_Word w = new DTO_Word();
////					w = sa.getItem(arg2);
////					String str = w.getWord();
////					Intent i = new Intent(AllEngDic_Word.this, AllEngDic_Home.class);
////					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////					i.putExtra("AllEngDic_Word", str);
////					startActivity(i);
////					finish();
////				}
////
////			});
//		} catch (Exception e) {
//			//showMsg("저장된 값이 없습니다.");
//		}
//	}
	
	/**
	 * 모두 삭제
	 */
	private void delDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.guitar);
		builder.setTitle("주의");
		builder.setMessage("정말로 모두 삭제하시겠습니까? \n (복구가 절대 되지 않습니다!)");
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// showMsg("which : "+ which);
				if (which == -1) {
					db = new AllEngDic_dbHandler(AllEngDic_Word.this);
					db.dropT();
					dataList();
				} else {
				}
			}
		};

		builder.setPositiveButton("예", listener);
		builder.setNegativeButton("아니오", listener);
		builder.show();
	}
	private void delRollDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.guitar);
		builder.setTitle("주의");
		builder.setMessage("즐겨찾기 목록을 [백업] 하신 후에 [복구] 하세요. \n ( '백업'하셨으면, '예' 버튼을 누르세요. )");
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// showMsg("which : "+ which);
				if (which == -1) {
					rollBackProcess();
				} else {
				}
			}
		};

		builder.setPositiveButton("예", listener);
		builder.setNegativeButton("아니오", listener);
		builder.show();
	}
	/**
	 * 하나만 삭제
	 */
	private DTO_Word w;
	private Vector<DTO_Word> vec = new Vector<DTO_Word>();
	
	private void delDialog2() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.guitar);
		builder.setTitle("주의");
		builder.setMessage("정말로 삭제하시겠습니까? \n (복구가 절대 되지 않습니다!)");
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// showMsg("which : "+ which);
				if (which == -1) {
					Iterator<Integer> keys2 = check.keySet().iterator();
					
					db = new AllEngDic_dbHandler(AllEngDic_Word.this);
					vec = db.dataRead();
					
					while(keys2.hasNext()){
			            Integer key = keys2.next();
			            Boolean chk = check.get(key);
			            if(chk){ // true면 삭제 한다.
			            	w = vec.get(key);
			            	db.dataDelete(w.getIdx()+"");
			            }
						Log.d("AllEngDic",  key + "," + chk.toString());
				    }
					check.clear();
					dataList();
					
					/*Vector<DTO_Word> vec = null;
					db = new AllEngDic_dbHandler(getParent());
					vec = db.dataRead();
					DTO_Word w = new DTO_Word();
					for(int i=0; i<vec.size(); i++){
						// 체크박스가 활성화 된것만 삭제 한다.
						
					}*/
				} else {
				}
			}
		};
		
		builder.setPositiveButton("예", listener);
		builder.setNegativeButton("아니오", listener);
		builder.show();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		Intent i = new Intent(getApplication(), AllEngDic_Home.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}

	private void endDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.guitar);
		builder.setTitle("종료");
		builder.setMessage("종료하시겠습니까?");
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

		builder.setPositiveButton("예", listener);
		builder.setNegativeButton("아니오", listener);
		builder.show();
	}
}
