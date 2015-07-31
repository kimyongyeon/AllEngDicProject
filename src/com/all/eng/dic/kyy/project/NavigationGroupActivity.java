package com.all.eng.dic.kyy.project;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NavigationGroupActivity extends ActivityGroup {
    ArrayList<View> history; // View들을 관리하기 위한 List
   NavigationGroupActivity group; // Activity들이 접근하기 위한 Group
    @Override
   protected void onCreate(Bundle savedInstanceState) {
         // TODO Auto-generated method stub
         super.onCreate(savedInstanceState);
         history = new ArrayList<View>();
         group = this;
   }
    public void changeView(View v)  { // 동일한 Level의 Activity를 다른 Activity로 변경하는 경우

         history.remove(history.size()-1);
         history.add(v);
         setContentView(v);
   }
   public void replaceView(View v) {   // 새로운 Level의 Activity를 추가하는 경우
         history.add(v);   
         setContentView(v); 
   }   
   public void back() { // Back Key가 눌려졌을 경우에 대한 처리
         if(history.size() > 1) {   
             history.remove(history.size()-1);   
             setContentView(history.get(history.size()-1)); 
         }
        else {   
             //finish(); // 최상위 Level의 경우 TabActvity를 종료해야 한다.
        	/*Intent i = new Intent(this,UTOPIA_Home_Main_Group.class); // 초급 
    		startActivity(i);
    		finish();*/
        	endDialog();
         }   
   }  
   @Override  
   public void onBackPressed() { // Back Key에 대한 Event Handler  
         group.back();   
         return;
   }
   
   private void endDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
       //builder.setIcon(R.drawable.guitar);
       builder.setTitle("종료");
       builder.setMessage("정말로 종료하시겠습니까?");
       DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				//showMsg("which : "+ which);
				if(which == -1){
					finish();
				}else{
				}
			}
		};	
       
       builder.setPositiveButton("예", listener);
       builder.setNegativeButton("아니오", listener);
       builder.show();	
	}
}
