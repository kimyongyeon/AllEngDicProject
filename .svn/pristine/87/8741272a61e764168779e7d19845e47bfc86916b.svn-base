package com.all.eng.dic.kyy.project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Internet_Check {
	private Context c;

	public Internet_Check(Context c) {
		this.c = c;
	}
	
	public boolean internetChk() {
		ConnectivityManager manager =
		(ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mobile.isConnected() || wifi.isConnected()) {
			// 어디 한군데라도 연결되어 있는 경우
			return true;
		} else {
			// 아무 네트워크에도 연결안되어 있는 경우
			return false;
		}
	}
}
