package com.all.eng.dic.kyy.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class Util {
	
	private Vector<DTO_Word> vec = new Vector<DTO_Word>();
	private DTO_Word d = new DTO_Word();
	
	private Vector<DTO_Icon> iconVec = new Vector<DTO_Icon>();
	private DTO_Icon i = new DTO_Icon();
	
	/**
	 * 백업하기
	 * @param word
	 * @param date
	 * @param phone
	 * @return
	 */
	public String postBackup(String[] word, String[] date, String phone) {
		// Create a new HtpClient and Post Header
		String msg = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);// 두번째 인자가 타임아웃
		HttpPost httppost = new HttpPost("http://kyy82.cafe24.com/multi_english/favor_backup.php");
		httppost.setParams(params);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
			String strWord = "";
			String strDate = "";
			
			for(int i=0; i<word.length; i++)
				strWord = strWord + word[i] + "|";
			
			nameValuePairs.add(new BasicNameValuePair("word", strWord)); // 단어
			
			for(int i=0; i<date.length; i++)
				strDate = strDate + date[i] + "|";
				
			nameValuePairs.add(new BasicNameValuePair("date", strDate)); // 날짜
			
			nameValuePairs.add(new BasicNameValuePair("phone", phone)); // 폰
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "euc-kr"));
			// Execute HTTP Post Request
			HttpResponse response = (HttpResponse) httpclient.execute(httppost);
			// String value2 = getResponseContent(response);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				// 받은 값.
				msg = inputStreamToString(instream, response);
				
				Log.d("kimyongyeon", "server value : " + msg + " / " + word.length);
			}

			return msg;
		} catch (ClientProtocolException e) {
			return msg;
			// TODO Auto-generated catch block
		} catch (IOException e) {
			return msg;
			// TODO Auto-generated catch block
		}
	}
	/**
	 * 복구하기
	 */
	public Vector<DTO_Word> postRollBack(String phone) {
		// Create a new HtpClient and Post Header
		String temp = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);// 두번째 인자가 타임아웃
		HttpPost httppost = new HttpPost("http://kyy82.cafe24.com/multi_english/favor_rollback.php"); // 주소만 적는다.
		httppost.setParams(params);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			// 파라미터들을 넘긴다.
			nameValuePairs.add(new BasicNameValuePair("phone", phone)); // 폰 번호
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
			// Execute HTTP Post Request
			HttpResponse response = (HttpResponse) httpclient.execute(httppost);
			// String value2 = getResponseContent(response);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				// 받은 값.
				try {
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser xpp = factory.newPullParser();
					xpp.setInput(instream, "euc-kr");

					int eventType = xpp.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						if (eventType == XmlPullParser.START_TAG) {
							// 이부분은 파싱하는 부분이기 때문에 알아서 넣어야 한다.
							if (xpp.getName().equals("word")) {
								// 1. 방을 만든다.
								// 2. 데이터 값을 넣는다.
								temp = xpp.nextText();
								d = new DTO_Word();
								d.setWord(temp);
							}
							if (xpp.getName().equals("date")) {
								// 1. 방을 만든다.
								// 2. 데이터 값을 넣는다.
								temp = xpp.nextText();
								d.setDate(temp);
								vec.add(d);
							}
						}
						eventType = xpp.next();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		
		return vec;
	}
	/**
	 * 설정 화면 신규아이콘 및 제목 다운로드
	 * @param phone
	 * @return
	 */
	public Vector<DTO_Icon> postAppUpgrade(String phone) {
		// Create a new HtpClient and Post Header
		String temp = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);// 두번째 인자가 타임아웃
		HttpPost httppost = new HttpPost("http://kyy82.cafe24.com/kyy_update/add.php"); // 주소만 적는다.
		httppost.setParams(params);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			// 파라미터들을 넘긴다.
			nameValuePairs.add(new BasicNameValuePair("phone", phone)); // 폰 번호
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "euc-kr"));
			// Execute HTTP Post Request
			HttpResponse response = (HttpResponse) httpclient.execute(httppost);
			// String value2 = getResponseContent(response);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				// 받은 값.
				try {
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser xpp = factory.newPullParser();
					xpp.setInput(instream, "euc-kr");

					int eventType = xpp.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						if (eventType == XmlPullParser.START_TAG) {
							// 이부분은 파싱하는 부분이기 때문에 알아서 넣어야 한다.
							if (xpp.getName().equals("no")) {
								// 1. 방을 만든다.
								// 2. 데이터 값을 넣는다.
								temp = xpp.nextText();
								i = new DTO_Icon();
								i.setSeq(temp);
							}
							if (xpp.getName().equals("icon_name")) {
								// 1. 방을 만든다.
								// 2. 데이터 값을 넣는다.
								temp = xpp.nextText();
								i.setIcon_name(temp);
							}
							if (xpp.getName().equals("package_name")) {
								// 1. 방을 만든다.
								// 2. 데이터 값을 넣는다.
								temp = xpp.nextText();
								i.setPackage_name(temp);
							}
							if (xpp.getName().equals("app_name")) {
								// 1. 방을 만든다.
								// 2. 데이터 값을 넣는다.
								temp = xpp.nextText();
								i.setApp_name(temp);
								iconVec.add(i);
							}
						}
						eventType = xpp.next();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		
		return iconVec;
	}

	private String inputStreamToString(InputStream is, HttpResponse response) {
		String s = "";
		String line = "";

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((line = rd.readLine()) != null) {
				s += line;
			}
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Return full string
		return s;
	}
}
