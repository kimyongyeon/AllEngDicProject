package com.all.eng.dic.kyy.project;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class NavigationActivity extends Activity {
    
	public void goNextHistory(String id,Intent intent)  { //앞으로 가기 처리
            NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
            View view = parent.group.getLocalActivityManager()
                 .startActivity(id,intent)   
                 .getDecorView();   
              parent.group.replaceView(view);
   }
    
    @Override
   public void onBackPressed() { //뒤로가기 처리
              NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
              parent.back();
   }
}