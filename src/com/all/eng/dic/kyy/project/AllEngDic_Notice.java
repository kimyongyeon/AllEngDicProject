package com.all.eng.dic.kyy.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

public class AllEngDic_Notice extends Activity{
	WebView noticeWv;
	private WebSettings ws;
	ImageButton setBack;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allengdic_notice);
		
		setBack = (ImageButton) findViewById(R.id.setBack);
		
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
		
		noticeWv = (WebView) findViewById(R.id.noticeWv);
		noticeWv.setWebViewClient(new WebViewClient()); //Áß¿ä
        ws = noticeWv.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDefaultTextEncodingName("utf-8");
        //ws.setBuiltInZoomControls(true);
        
        remote();
	}
	
	private void remote(){
    	String strURL = m_url;
    	if(strURL != null) strURL = strURL.trim();
    	if(strURL.length() >= 7){
	    	String  head= strURL.substring(0, 7);
    	    if(!head.equals("http://")){
    	    	strURL = "http://"+strURL;
    	    }
    	}
    	
    	//showMsg(strURL);
    	noticeWv.loadUrl(strURL);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(!isNetWork())
			m_url = "";
		else
			m_url = "http://kyy82.cafe24.com/kyy_update/notice.php";
			
	}   
	
	private String m_url = "http://kyy82.cafe24.com/kyy_update/notice.php";
	
	private Boolean isNetWork(){
		   
    	ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
 
    	boolean isMobileAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
	    boolean isMobileConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
	    boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
	    boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
	            
	    if ((isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect)){
	    	return true;
    	}else{
	    	return false;
    	}
    }

}
