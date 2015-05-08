package com.mistletoe.flappyrabbit;


import com.mistletoe.flappyrabbit.view.GameView;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
	private GameView mGameView;
	private Intent Serviceintent = new Intent("com.angel.Android.MUSIC"); 
	private long exitTime = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		SharedPreferences sp = getApplicationContext().getSharedPreferences("BestScore",MODE_PRIVATE);  
//		SharedPreferences sp = this.getSharedPreferences("BestScore", MODE_PRIVATE);  //存储
//		Editor editor = sp.edit();
		
//		startService(Serviceintent);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mGameView = new GameView(this, null);
		setContentView(mGameView);
//		editor.putInt("best", mGameView.getBestScore());
//		editor.commit();

	}
	protected void onDestroy() { 
		// TODO Auto-generated method stub 
		super.onDestroy(); 
		stopService(Serviceintent); 
		System.exit(0); 
		}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
