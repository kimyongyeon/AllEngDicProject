package com.all.eng.dic.kyy.project;

import java.util.Vector;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class AllEngDic_dbHandler {
	private static final String DBNAME = "w_database.db";
	// 테이블은 대문자가 들어갈 수 없다.
	private static final String TB_WORD = "word";
	private static final String TB_SEARCH = "search";
	private static final String TB_POTAL = "potal";
	private SQLiteDatabase db;
	Activity activity;

	AllEngDic_dbHandler(Activity activity) {
		this.activity = activity;
		dbConnection(); // DB연결
		//dropT();
		createT();
	}

	// DB Connection
	private void dbConnection() {
		db = activity.openOrCreateDatabase(DBNAME, activity.MODE_PRIVATE, null);
	}

	// DB Close
	public void dbClose() {
		db.close();
	}

	// DB Delete
	public void dbDel() {
		activity.deleteDatabase(DBNAME);
	}

	// Table 삭제(일괄삭제)
	public void dropT() {
		try {
			String sql1 = "drop table " + TB_WORD;
			db.execSQL(sql1);
		} catch (android.database.SQLException se) {
		}
		try {
			String sql1 = "drop table " + TB_POTAL;
			db.execSQL(sql1);
		} catch (android.database.SQLException se) {
		}
		try {
			String sql1 = "drop table " + TB_SEARCH;
			//db.execSQL(sql1);
		} catch (android.database.SQLException se) {
		}
	}

	// Table 삭제(일괄삭제)
	public void dropT2() {
		try {
			String sql1 = "drop table " + TB_SEARCH;
			db.execSQL(sql1);
		} catch (android.database.SQLException se) {
		}
	}

	private String sql;

	private void createT() {

		// 검색한 단어를 저장한다.
		sql = "create table " + TB_SEARCH
		+ "(_ID integer primary key autoincrement, " + "word text,"
		+ "date text, chk text);";
		try {
			db.execSQL(sql);
		} catch (android.database.SQLException se) {
		}

		// 나의단어장 테이블
		sql = "create table " + TB_WORD
				+ "(_ID integer primary key autoincrement, " + "word text,"
				+ "date text, chk text);";
		try {
			db.execSQL(sql);
		} catch (android.database.SQLException se) {
		}

		// 포털사이트 순위 및 사용유무 테이블
		sql = "create table " + TB_POTAL
				+ "(id integer primary key, " + "name text,"
				+ "addr text, chk text);";
		try {
			db.execSQL(sql);
		} catch (android.database.SQLException se) {
		}
	}

	// SQLlite 데이터 삭제(한행삭제)
	public void dataDelete(String id) {
		String sql = "delete from " + TB_WORD + " where _ID=?";

		String _idStr = id;
		if (_idStr != null)
			_idStr = _idStr.trim();
		int _id = Integer.parseInt(_idStr);

		try {
			Object bindArgs[] = { _id };
			db.execSQL(sql, bindArgs);
		} catch (android.database.SQLException se) {
		}
	}

	public void dataPotalDelete(String id) {
		String sql = "delete from " + TB_POTAL + " where id=?";

		String _idStr = id;
		if (_idStr != null)
			_idStr = _idStr.trim();
		int _id = Integer.parseInt(_idStr);

		try {
			Object bindArgs[] = { _id };
			db.execSQL(sql, bindArgs);
		} catch (android.database.SQLException se) {
		}
	}

	public void dataSearchDelete(String id) {
		String sql = "delete from " + TB_SEARCH + " where _ID=?";

		String _idStr = id;
		if (_idStr != null)
			_idStr = _idStr.trim();
		int _id = Integer.parseInt(_idStr);

		try {
			Object bindArgs[] = { _id };
			db.execSQL(sql, bindArgs);
		} catch (android.database.SQLException se) {
		}
	}



	// SQLlite 데이터 저장
	// name:이름, contents:컨텐츠명, juso:URL주소
	private Object bind[];
	private String m_insertSQL;

	public void dataSave(Object sqlData) {
		String passColums = "(word, date, chk) values(?,?,?);";

		String sql = "insert into " + TB_WORD + passColums;
		DTO_Word w = new DTO_Word();
		w = (DTO_Word) sqlData;

		String  flag = "";

		if(w.isChk()){
			flag = "1";
		}else{
			flag = "0";
		}

		Object bindArgs[] = { w.getWord(), w.getDate(), flag };
		m_insertSQL = sql;
		bind = bindArgs;

		try {
			db.execSQL(m_insertSQL, bind);
			Toast.makeText(this.activity, "데이터저장 성공!!!", 0);
		} catch (android.database.SQLException se) {
		}

	}

	public void dataSearchSave(Object sqlData) {
		String passColums = "(word, date, chk) values(?,?,?);";

		String sql = "insert into " + TB_SEARCH + passColums;
		DTO_Word w = new DTO_Word();
		w = (DTO_Word) sqlData;

		String  flag = "";

		if(w.isChk()){
			flag = "1";
		}else{
			flag = "0";
		}

		Object bindArgs[] = { w.getWord(), w.getDate(), flag };
		m_insertSQL = sql;
		bind = bindArgs;

		try {
			db.execSQL(m_insertSQL, bind);
			Toast.makeText(this.activity, "데이터저장 성공!!!", 0);
		} catch (android.database.SQLException se) {
			Toast.makeText(this.activity, "데이터저장 실패!!!", 0);
		}

	}

	public void dataPotalSave(Object sqlData) {
		String Colums = "(id, name, addr, chk) values(?,?,?,?);";

		String sql = "insert into " + TB_POTAL + Colums;
		DTO_Address a = new DTO_Address();
		a = (DTO_Address) sqlData;
		String  flag = "";

		if(a.isChk()){
			flag = "1";
		}else{
			flag = "0";
		}
		int idx = Integer.parseInt(a.getIdx());
		Object bindArgs[] = { idx, a.getName(), a.getAddr(), flag };
		m_insertSQL = sql;
		bind = bindArgs;

		try {
			db.execSQL(m_insertSQL, bind);
			Toast.makeText(this.activity, "데이터저장 성공!!!", 0);
		} catch (android.database.SQLException se) {
			String str = se.getMessage();
		}

	}

	private String m_updateSQL;
	public void dataPotalUpdate(Object sqlData) {
		String passColums = " set chk=?";
		String where = " where id=?;";
		String sql = "update " + TB_POTAL + passColums + where;

		DTO_Address a = new DTO_Address();
		a = (DTO_Address) sqlData;
		String  flag = "";

		if(a.isChk()){
			flag = "1";
		}else{
			flag = "0";
		}
		int idx = Integer.parseInt(a.getIdx());
		Object bindArgs[] = { flag, idx };

		m_updateSQL = sql;
		bind = bindArgs;

		try {
			db.execSQL(m_updateSQL, bind);
			//Toast.makeText(this.activity, "데이터수정 성공!!!", 0).show();
		} catch (android.database.SQLException se) {
			Toast.makeText(this.activity, se.getMessage(), 0).show();
		}
	}
	public void dataUpdate(Object sqlData) {
		String passColums = " set chk=?";
		String where = " where _ID=?;";
		String sql = "update " + TB_WORD + passColums + where;

		DTO_Word a = new DTO_Word();
		a = (DTO_Word) sqlData;
		String  flag = "";

		if(a.isChk()){
			flag = "1";
		}else{
			flag = "0";
		}
		int idx = a.getIdx();
		Object bindArgs[] = { flag, idx };

		m_updateSQL = sql;
		bind = bindArgs;

		try {
			db.execSQL(m_updateSQL, bind);
			//Toast.makeText(this.activity, "데이터수정 성공!!!", 0).show();
		} catch (android.database.SQLException se) {
			Toast.makeText(this.activity, se.getMessage(), 0).show();
		}
	}
	public void dataSearchUpdate(Object sqlData) {
		String passColums = " set chk=?";
		String where = " where _ID=?;";
		String sql = "update " + TB_SEARCH + passColums + where;

		DTO_Word a = new DTO_Word();
		a = (DTO_Word) sqlData;
		String  flag = "";

		if(a.isChk()){
			flag = "1";
		}else{
			flag = "0";
		}
		int idx = a.getIdx();
		Object bindArgs[] = { flag, idx };

		m_updateSQL = sql;
		bind = bindArgs;

		try {
			db.execSQL(m_updateSQL, bind);
			//Toast.makeText(this.activity, "데이터수정 성공!!!", 0).show();
		} catch (android.database.SQLException se) {
			Toast.makeText(this.activity, se.getMessage(), 0).show();
		}
	}

	// SQLlite 데이터 읽기 == > List 객체에 저장 ==> ListView에 뿌림.
	// null : 리드할 내용이 없슴.
	private String m_sqlRead;

	public Vector<DTO_Word> dataRead() {
		// password table read
		DTO_Word w = new DTO_Word();
		Vector<DTO_Word> v = new Vector<DTO_Word>();
		m_sqlRead = "select * from " + TB_WORD + " where 1 =1 order by date desc";

		Cursor c = db.rawQuery(m_sqlRead, null);

		String temp="";

		if (c.moveToFirst()) {
			do {
				// 여기에 DB에서 읽어본 데이터를 mylist에 저장해야 한다.
				w = new DTO_Word();
				w.setIdx(c.getInt(0));
				w.setWord(c.getString(1));
				w.setDate(c.getString(2));

				temp = c.getString(3);
				if(temp.equals("1")){
					w.setChk(true);
				}else{
					w.setChk(false);
				}

				v.add(w);
			} while (c.moveToNext());
		}
		c.close();

		return v;
	}

	public Vector<DTO_Word> dataSearchRead() {
		// password table read
		DTO_Word w = new DTO_Word();
		Vector<DTO_Word> v = new Vector<DTO_Word>();
		m_sqlRead = "select * from " + TB_SEARCH + " where 1 =1 order by date desc";

		Cursor c = db.rawQuery(m_sqlRead, null);

		String temp="";

		if (c.moveToFirst()) {
			do {
				// 여기에 DB에서 읽어본 데이터를 mylist에 저장해야 한다.
				w = new DTO_Word();
				w.setIdx(c.getInt(0));
				w.setWord(c.getString(1));
				w.setDate(c.getString(2));

				temp = c.getString(3);
				if(temp.equals("1")){
					w.setChk(true);
				}else{
					w.setChk(false);
				}

				v.add(w);
			} while (c.moveToNext());
		}
		c.close();

		return v;
	}
	/**
	 * 정렬방식
	 * @return
	 */
	public Vector<DTO_Word> dataRead(String sort) {
		// password table read
		DTO_Word w = new DTO_Word();
		Vector<DTO_Word> v = new Vector<DTO_Word>();
		if("ASC".equals(sort)){ // 오름차순
			m_sqlRead = "select * from " + TB_WORD + " where 1=1 order by word asc";
		}else if("DESC".equals(sort)){ // 내림차순
			m_sqlRead = "select * from " + TB_WORD + " where 1=1 order by word desc";
		}
		Cursor c = db.rawQuery(m_sqlRead, null);
		String temp="";
		if (c.moveToFirst()) {
			do {
				// 여기에 DB에서 읽어본 데이터를 mylist에 저장해야 한다.
				w = new DTO_Word();
				w.setIdx(c.getInt(0));
				w.setWord(c.getString(1));
				w.setDate(c.getString(2));

				temp = c.getString(3);
				if(temp.equals("1")){
					w.setChk(true);
				}else{
					w.setChk(false);
				}

				v.add(w);
			} while (c.moveToNext());
		}
		c.close();

		return v;
	}

	public Vector<DTO_Address> dataPotalRead() {
		// password table read
		DTO_Address a = new DTO_Address();
		Vector<DTO_Address> v = new Vector<DTO_Address>();
		m_sqlRead = "select * from " + TB_POTAL;

		Cursor c = db.rawQuery(m_sqlRead, null);
		String temp = "";
		if (c.moveToFirst()) {
			do {
				// 여기에 DB에서 읽어본 데이터를 mylist에 저장해야 한다.
				a = new DTO_Address();
				a.setIdx(c.getInt(0)+"");
				a.setName(c.getString(1));
				a.setAddr(c.getString(2));
				temp = c.getString(3);
				if(temp.equals("1")){
					a.setChk(true);
				}else{
					a.setChk(false);
				}
				v.add(a);
			} while (c.moveToNext());
		}
		c.close();

		return v;
	}
}
