package com.all.eng.dic.kyy.project;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class NavigationActivity extends Activity {
    
	public void goNextHistory(String id,Intent intent)  { //������ ���� ó��
            NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
            View view = parent.group.getLocalActivityManager()
                 .startActivity(id,intent)   
                 .getDecorView();   
              parent.group.replaceView(view);
   }
    
    @Override
   public void onBackPressed() { //�ڷΰ��� ó��
              NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
              parent.back();
   }
}