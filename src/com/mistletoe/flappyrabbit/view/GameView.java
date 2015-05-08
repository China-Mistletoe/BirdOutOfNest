package com.mistletoe.flappyrabbit.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mistletoe.flappyrabbit.MainActivity;
import com.mistletoe.flappyrabbit.R;
import com.mistletoe.flappyrabbit.element.Bird;
import com.mistletoe.flappyrabbit.element.BirdWorld;

public class GameView extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {

	private Bird mBird;
	private BirdWorld mBirdWorld;
	private List<Bitmap[]> mListBirdsSkin;
	private List<Bitmap[]> mListPipesSkin;
	private List<Bitmap> mListSkySkin;
	private Bitmap mGroundSkin;
	private Matrix mMatrix;
	private boolean mIsRunning;

	private SoundPool mSoundPool;
	private Map<String, Integer> mSoundMap;

	private GestureDetector mGestureDetector;

	// private boolean mIsPlay;
	private Paint mPaint;

	// 槲寄生：游戏结束判断
	public boolean isGameOver = false;
	private int getScore = 0; // 分数积累量的保存
	private int oldGetScore = 0;
	private int s = 0;
	private int s1 = 0;
	private int s2 = 0;
	private int s3 = 0;
	private int s4 = 0;
	private int oldS = 0;
	private int oldS2 = 0;
	private int soundNum = 0;
	private int soundNum2 = 0;
	private int soundNum3 = 0;
	private int soundNum4 = 0;
	public Bitmap gameOverBitmap, number00, number01, number02, number03,
			number04, number05, number06, number07, number08, number09, dead01,
			dead02, dead03, dieBird, dieBird2, dieBird_2, dieBird2_2,
			dieBird_3, dieBird2_3, myTitle, myDescribe, byMistletoe,
			score_panel, gameover_text, ready_text, number000, number001,
			number002, number003, number004, number005, number006, number007,
			number008, number009, medal_copper, medal_silver, medal_gold,
			medal_platinum,birdnest,tree;
	private List<Bitmap[]> mScoreNumber;
	private List<Bitmap[]> mScoreBoartNumber;
	private Bitmap[] scoreNum = null;
	private Bitmap[] scoreBoartNum = null;
	private Bitmap bitmap = null;
	private List<Bitmap[]> mDeadView;
	private Bitmap[] deadView = null;
	private Bitmap bitmap1 = null;
	private int myBestScore;
	private Editor editor;
	private SharedPreferences sp;
	private int dayTime = 0;
	private int nightTime = 0;
	private Rect bound;
//	private int movingtree=0;
//	private int movingnest=0;
	private Intent Serviceintent = new Intent("com.angel.Android.MUSIC"); 

	public boolean getDead() {
		return mBirdWorld.ifDead();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setStrokeWidth(6);
		
		sp = getContext().getSharedPreferences("BestScore", 0);
		editor = sp.edit();
		mMatrix = new Matrix();
		getHolder().addCallback(this);
		mGestureDetector = new GestureDetector(getContext(),
				new GameGestureDetector());
		loadSoundPool();
		gameOverBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.text_game_over);
		number00 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_00);
		number01 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_01);
		number02 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_02);
		number03 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_03);
		number04 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_04);
		number05 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_05);
		number06 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_06);
		number07 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_07);
		number08 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_08);
		number09 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_large_09);
		mScoreNumber = new ArrayList<>();
		scoreNum = new Bitmap[10];
		for (int j = 0; j < 10; j++) {
			bitmap = BitmapFactory.decodeResource(getContext().getResources(),
					R.drawable.number_large_00 + j);
			scoreNum[j] = Bitmap.createBitmap(bitmap);
		}
		mScoreNumber.add(scoreNum);

		number000 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_00);
		number001 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_01);
		number002 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_02);
		number003 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_03);
		number004 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_04);
		number005 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_05);
		number006 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_06);
		number007 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_07);
		number008 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_08);
		number009 = BitmapFactory.decodeResource(getResources(),
				R.drawable.number_middle_09);
		mScoreBoartNumber = new ArrayList<>();
		scoreBoartNum = new Bitmap[10];
		for (int j = 0; j < 10; j++) {
			bitmap = BitmapFactory.decodeResource(getContext().getResources(),
					R.drawable.number_middle_00 + j);
			scoreBoartNum[j] = Bitmap.createBitmap(bitmap);
		}
		mScoreBoartNumber.add(scoreNum);

		dead01 = BitmapFactory.decodeResource(getResources(),
				R.drawable.blink_00);
		dead02 = BitmapFactory.decodeResource(getResources(),
				R.drawable.blink_01);
		dead03 = BitmapFactory.decodeResource(getResources(),
				R.drawable.blink_02);
		mDeadView = new ArrayList<>();
		deadView = new Bitmap[3];
		for (int j = 0; j < 3; j++) {
			bitmap1 = BitmapFactory.decodeResource(getContext().getResources(),
					R.drawable.blink_00 + j);
			deadView[j] = Bitmap.createBitmap(bitmap1);
		}
		mDeadView.add(deadView);
		dieBird = BitmapFactory.decodeResource(getResources(),
				R.drawable.birddie);
		dieBird2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.birddie2);
		dieBird_2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.birddie_2);
		dieBird2_2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.birddie2_2);
		dieBird_3 = BitmapFactory.decodeResource(getResources(),
				R.drawable.birddie_3);
		dieBird2_3 = BitmapFactory.decodeResource(getResources(),
				R.drawable.birddie2_3);

		myTitle = BitmapFactory
				.decodeResource(getResources(), R.drawable.title);
		myDescribe = BitmapFactory.decodeResource(getResources(),
				R.drawable.tutorial);
		byMistletoe = BitmapFactory.decodeResource(getResources(),
				R.drawable.byus2);

		score_panel = BitmapFactory.decodeResource(getResources(),
				R.drawable.score_panel);
		medal_copper = BitmapFactory.decodeResource(getResources(),
				R.drawable.medals_3);
		medal_silver = BitmapFactory.decodeResource(getResources(),
				R.drawable.medals_2);
		medal_gold = BitmapFactory.decodeResource(getResources(),
				R.drawable.medals_1);
		medal_platinum = BitmapFactory.decodeResource(getResources(),
				R.drawable.medals_0);
		gameover_text = BitmapFactory.decodeResource(getResources(),
				R.drawable.text_game_over);
		ready_text = BitmapFactory.decodeResource(getResources(),
				R.drawable.text_ready);
		birdnest = BitmapFactory.decodeResource(getResources(),
				R.drawable.bird_nest);
		tree = BitmapFactory.decodeResource(getResources(),
				R.drawable.tree);
	}

	private Rect calcBirdShotBound() {
		Bitmap birdSkin = mListBirdsSkin.get(0)[0];
		Rect bound = new Rect();
		bound.set(getWidth() / 3 - birdSkin.getWidth() / 2, getHeight() / 2
				- birdSkin.getHeight() / 2,
				getWidth() / 3 + birdSkin.getWidth() / 2, getHeight() / 2
						+ birdSkin.getHeight() / 2);
		return bound;
	}

	private Rect calcBirdInitBound() {
		Bitmap birdSkin = mListBirdsSkin.get(0)[0];
		Rect bound = new Rect();
		bound.set(getWidth() / 2 - birdSkin.getWidth() / 2, getHeight() * 2 / 5
				- birdSkin.getHeight() / 2,
				getWidth() / 2 + birdSkin.getWidth() / 2, getHeight() * 2 / 5
						+ birdSkin.getHeight() / 2);
		return bound;
	}

	private void loadSoundPool() {
		mSoundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
		AssetManager am = getContext().getAssets();
		mSoundMap = new HashMap<>();
		try {
			mSoundMap
					.put("Die", mSoundPool.load(am.openFd("sound/Die.wav"), 1));
			mSoundMap
					.put("Hit", mSoundPool.load(am.openFd("sound/Hit.wav"), 1));
			mSoundMap.put("Hit2",
					mSoundPool.load(am.openFd("sound/Hit2.mp3"), 1));
			mSoundMap.put("Point",
					mSoundPool.load(am.openFd("sound/Point.wav"), 1));
			mSoundMap.put("Swooshing",
					mSoundPool.load(am.openFd("sound/Swooshing.wav"), 1));
			mSoundMap.put("Wing",
					mSoundPool.load(am.openFd("sound/Wing.wav"), 1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadBackgroundSkin() {
		mListSkySkin = new ArrayList<>();
		Bitmap skyOrigin, skyScale, sky;
		skyOrigin = BitmapFactory.decodeResource(getContext().getResources(),
				R.drawable.bg_day);
		skyScale = Bitmap.createScaledBitmap(skyOrigin, getWidth(),
				getHeight(), false);
		sky = Bitmap.createBitmap(skyScale, 0, 0, skyScale.getWidth(),
				getHeight() * 4 / 5);
		mListSkySkin.add(sky);
		skyOrigin.recycle();
		skyScale.recycle();

		skyOrigin = BitmapFactory.decodeResource(getContext().getResources(),
				R.drawable.bg_night);
		skyScale = Bitmap.createScaledBitmap(skyOrigin, getWidth(),
				getHeight(), false);
		sky = Bitmap.createBitmap(skyScale, 0, 0, skyScale.getWidth(),
				getHeight() * 4 / 5);
		mListSkySkin.add(sky);
		skyOrigin.recycle();
		skyScale.recycle();

		Bitmap groundOrigin;
		groundOrigin = BitmapFactory.decodeResource(
				getContext().getResources(), R.drawable.land);
		mGroundSkin = Bitmap.createScaledBitmap(groundOrigin, getWidth(),
				getHeight() * 1 / 5, false);
		groundOrigin.recycle();
	}

	private void loadBirdsSkin() {
		Bitmap[] birds = null;
		Bitmap bitmap = null;
		int width = getWidth() / 6;
		int height = getHeight() * 3 / 32;

		mListBirdsSkin = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			birds = new Bitmap[3];
			for (int j = 0; j < 3; j++) {
				bitmap = BitmapFactory.decodeResource(getContext()
						.getResources(), R.drawable.bird0_0 + i * 3 + j);
				birds[j] = Bitmap.createScaledBitmap(bitmap, width, height,
						false);
				bitmap.recycle();
			}
			mListBirdsSkin.add(birds);
		}
	}

	private void loadPipesSkin() {
		Bitmap[] pipes = null;
		Bitmap bitmap = null;
		int width = getWidth() * 13 / 72;
		int height = getHeight() * 5 / 8;

		mListPipesSkin = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			pipes = new Bitmap[2];
			for (int j = 0; j < 2; j++) {
				bitmap = BitmapFactory.decodeResource(getContext()
						.getResources(), R.drawable.pipe2_down + i * 2 + j);
				pipes[j] = Bitmap.createScaledBitmap(bitmap, width, height,
						false);
				bitmap.recycle();
			}
			mListPipesSkin.add(pipes);
		}
	}

	@Override
	public void draw(Canvas canvas) {

		// canvas.drawColor(0xFF000000);
		mBirdWorld.draw(canvas);
		// canvas.drawLine(270, 0, 270, 1920, mPaint);
		// canvas.drawLine(450, 0, 450, 1920, mPaint);
		if(mBirdWorld.dead){
			getContext().stopService(Serviceintent); 
		}
		if(!mBirdWorld.mIsStandby){
			getContext().startService(Serviceintent);
		}
		
		if (mBirdWorld.IsStandBy() && !mBirdWorld.mIsDeadOnce()) {
			canvas.drawBitmap(tree, -150,
					120, null);
			canvas.drawBitmap(myTitle, getWidth() / 4 +15,
					getHeight() / 4 - 50, null);
			canvas.drawBitmap(byMistletoe, getWidth() * 3 / 5, getHeight() / 3,
					null);
			canvas.drawBitmap(ready_text, getWidth() / 4 - 20,
					getHeight() / 4 + 750, null);
			canvas.drawBitmap(myDescribe, getWidth() * 1 / 3+20,
					getHeight() * 3 / 6-160 , null);
			canvas.drawBitmap(birdnest, getWidth() / 4+200,
					getHeight() / 4 + 250, null);
		}
		if (mBirdWorld.IsStandBy() && mBirdWorld.mIsDeadOnce()) {
			canvas.drawBitmap(score_panel, getWidth() / 5, 700, null);
			canvas.drawBitmap(scoreBoartNum[s4], 730, 805, null);
			canvas.drawBitmap(scoreBoartNum[s2], 770, 805, null);
			canvas.drawBitmap(scoreBoartNum[s], 810, 805, null);
			canvas.drawBitmap(gameover_text, getWidth() / 5 + 50, 450, null);
			Paint p1 = new Paint();
			p1.setAntiAlias(true);
			p1.setColor(Color.WHITE);
			p1.setTextSize(70);// 设置字体大小
			p1.setFakeBoldText(true);//加粗
			myBestScore = sp.getInt("best", 0);
			canvas.drawText("" + myBestScore, 767, 1000, p1);
			if (getScore < 10) {
				canvas.drawBitmap(medal_copper, 326, 835, null);
			} else if (getScore >= 10 && getScore < 20) {
				canvas.drawBitmap(medal_silver, 326, 835, null);
			} else if (getScore >= 20 && getScore < 30) {
				canvas.drawBitmap(medal_gold, 326, 835, null);
			} else if (getScore >= 30) {
				canvas.drawBitmap(medal_platinum, 326, 835, null);
			}
			if (getScore > sp.getInt("best", 0)) { // 存储最高分数
				editor.putInt("best", getScore);
				editor.commit();
			}
		}
		if (!mBirdWorld.ifDead()) {
			mBird.draw(canvas);
		} else {
			if (soundNum < 1) {
				soundNum++;
				int id0 = mSoundMap.get("Hit");
				mSoundPool.play(id0, 1f, 1f, 1, 0, 1f);
				int id1 = mSoundMap.get("Die");
				mSoundPool.play(id1, 1f, 1f, 1, 0, 1f);
			}
			float downBird = mBird.mBound.top += 35;
			if (dayTime - nightTime < 20) {
				if (downBird < getHeight() * 4 / 5 - 100) {
					canvas.drawBitmap(dieBird, mBird.mBound.left, downBird,
							null);
				} else {
					if (soundNum2 < 1) {
						soundNum2++;
						int id2 = mSoundMap.get("Hit2");
						mSoundPool.play(id2, 1f, 1f, 1, 0, 1f);
					}
					canvas.drawBitmap(dieBird2, mBird.mBound.left,
							getHeight() * 4 / 5 - 100, null);
				}
			} else if (dayTime - nightTime >= 20 && dayTime - nightTime < 40) {
				if (downBird < getHeight() * 4 / 5 - 100) {
					canvas.drawBitmap(dieBird_3, mBird.mBound.left, downBird,
							null);
				} else {
					if (soundNum3 < 1) {
						soundNum3++;
						int id2 = mSoundMap.get("Hit2");
						mSoundPool.play(id2, 1f, 1f, 1, 0, 1f);
					}
					canvas.drawBitmap(dieBird2_3, mBird.mBound.left,
							getHeight() * 4 / 5 - 100, null);
				}
			} else if (dayTime - nightTime >= 40) {
				if (downBird < getHeight() * 4 / 5 - 100) {
					canvas.drawBitmap(dieBird_2, mBird.mBound.left, downBird,
							null);
				} else {
					if (soundNum4 < 1) {
						soundNum4++;
						int id2 = mSoundMap.get("Hit2");
						mSoundPool.play(id2, 1f, 1f, 1, 0, 1f);
					}
					canvas.drawBitmap(dieBird2_2, mBird.mBound.left,
							getHeight() * 4 / 5 - 100, null);
				}
			}
		}
		// 槲寄生
		// Paint p2 = new Paint();
		// p2.setAntiAlias(true);
		// p2.setColor(Color.WHITE);
		// p2.setTextSize(100);// 设置字体大小
		// canvas.drawText("得分：" + BirdWorld.score, 200, 100, p2);

		// 昼夜循环
		dayTime = getScore;
		if (dayTime - nightTime < 20) {
			mBirdWorld.setSkySkin(mListSkySkin.get(0))
					.setGroundSkin(mGroundSkin)
					.setPipesSkin(mListPipesSkin.get(1)).setBird(mBird);
			mBird.setBirdsSkin(mListBirdsSkin.get(0));
		}
		if (dayTime - nightTime >= 20 && dayTime - nightTime < 40) {
			mBirdWorld.setSkySkin(mListSkySkin.get(1))
					.setGroundSkin(mGroundSkin)
					.setPipesSkin(mListPipesSkin.get(1)).setBird(mBird);
			mBird.setBirdsSkin(mListBirdsSkin.get(2));
		}
		if (dayTime - nightTime >= 40) {
			if (dayTime - nightTime == 60) {
				nightTime = dayTime;
			}
			mBirdWorld.setSkySkin(mListSkySkin.get(1))
					.setGroundSkin(mGroundSkin)
					.setPipesSkin(mListPipesSkin.get(0)).setBird(mBird);
			mBird.setBirdsSkin(mListBirdsSkin.get(1));
		}
		if(isGameOver){
			getScore=0;
		}

		// 满十计一分
		if (BirdWorld.score == 10) {
			getScore++;
			oldGetScore = BirdWorld.score;
		}
		if (BirdWorld.score - oldGetScore == 10) {
			getScore++;
			oldGetScore = BirdWorld.score;
		}
		if (!mBirdWorld.IsStandBy()) {
			s1 = getScore;
			if (s1 < 10) {
				canvas.drawBitmap(number00, 147, 200, null);
			}
			if (s1 - oldS == 10) {
				s2++;
				if (s2 > 9) {
					s2 = 0;
				}
				canvas.drawBitmap(scoreNum[s2], 147, 200, null);
				oldS = s1;
			}
			s = getScore;
			if (s > 9) {
				s = s % 10;
			}
			s3 = getScore;
			if (s3 < 100) {
				canvas.drawBitmap(number00, 67, 200, null);
			}
			if (s3 - oldS2 == 100) {
				s4++;
				if (s4 > 9) {
					s4 = 0;
				}
				canvas.drawBitmap(scoreNum[s4], 67, 200, null);
				oldS2 = s3;
			}
			canvas.drawBitmap(scoreNum[s4], 67, 200, null);
			canvas.drawBitmap(scoreNum[s2], 147, 200, null);
			canvas.drawBitmap(scoreNum[s], 227, 200, null);
		}
	}

	@Override
	public void run() {
		while (mIsRunning) {
			Canvas canvas = getHolder().lockCanvas();
			draw(canvas);
			getHolder().unlockCanvasAndPost(canvas);

			// if(isGameOver==true)
			// {
			// int id = mSoundMap.get("Die");
			// mSoundPool.play(id, 1f, 1f, 1, 1, 1f);
			// }
			sleep();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 必须返回这个事件给DestureDetector
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		loadBirdsSkin();
		loadPipesSkin();
		loadBackgroundSkin();

		Rect bound = calcBirdInitBound();
		mBird = new Bird().setBound(bound).setMatrix(mMatrix)
				.setBirdsSkin(mListBirdsSkin.get(0));
		mBird.makeStandby();
		
		mBirdWorld = new BirdWorld()
				.setBound(new Rect(0, 0, getWidth(), getHeight()))
				.setSkySkin(mListSkySkin.get(0)).setGroundSkin(mGroundSkin)
				.setPipesSkin(mListPipesSkin.get(1)).setBird(mBird).setTreeSkin(tree).setNestSkin(birdnest);
		mBirdWorld.makeStandby();
		
		mIsRunning = true;
		new Thread(this).start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mIsRunning = false;
	}

	// 包含多种手势检测，比如滑动、上拨等
	private class GameGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (mIsRunning && !mBirdWorld.dead) {
				if (mBirdWorld.IsStandBy()) {
					mBirdWorld.roll();
					Rect bound = calcBirdShotBound();
					synchronized (mBird) {
						mBird.setBound(bound).shot();
					}
				} else {
					mBird.shot();
					if (!mBirdWorld.ifDead()) {
						int id = mSoundMap.get("Wing");
						mSoundPool.play(id, 1f, 1f, 1, 0, 1f);
					}
				}
			}
			return true;
		}

		// public boolean onFling(MotionEvent e1, MotionEvent e2, float
		// velocityX,
		// float velocityY) {
		// // 当用户在屏幕上“拖动”时触发该方法
		// if(velocityX>10){
		// mBirdWorld.mIsStandby=true;
		// mBirdWorld.mIsDeadOnce=false;
		// mBirdWorld.dead=false;
		// mBirdWorld.mFrameCount=0;
		// }
		// return true;
		// }

	}
}
