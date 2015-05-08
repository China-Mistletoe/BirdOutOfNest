package com.mistletoe.flappyrabbit;


import com.mistletoe.flappyrabbit.view.GameView;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	private GameView mGameView;
	private Intent Serviceintent = new Intent("com.angel.Android.MUSIC"); 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		SharedPreferences sp = getApplicationContext().getSharedPreferences("BestScore",MODE_PRIVATE);  
//		SharedPreferences sp = this.getSharedPreferences("BestScore", MODE_PRIVATE);  //´æ´¢
//		Editor editor = sp.edit();
		startService(Serviceintent);
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
}
