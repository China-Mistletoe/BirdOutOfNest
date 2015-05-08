package com.mistletoe.flappyrabbit.element;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mistletoe.flappyrabbit.R;

public class BirdWorld {
	private static final int DEFAULT_ROLLING_SPEED = 30;

	private static final int SPEED_SCALE = 8;
	private Rect mBound;
	private int mGroundTop;

	private Bitmap mSkySkin,birdnest,tree;
	private Bitmap mGroundSkin;
	private Bitmap[] mPipesSkin;

	private List<PipePair> mTemplatePipeList;
	// 队列
	private Queue<PipePair> mPipePairQueue;
	private int mNextPipeFrameCount;

	private int mRollingSpeed;
	public boolean mIsStandby;
	public int mFrameCount;

	// 槲寄生
	public static int score = 0;
	private Bird mBird;
	public static final int DEFAULT_FLY_DISTANCE = 80;
	public boolean dead = false;
	public boolean mIsDeadOnce = false;
	private int movingtree=-150;
	private int movingnest=350;

	public BirdWorld setBird(Bird b) {
		mBird = b;
		return this;
	}

	public BirdWorld() {
		mRollingSpeed = DEFAULT_ROLLING_SPEED;
		mTemplatePipeList = new ArrayList<>();
		mPipePairQueue = new LinkedList<>();
	}

	public BirdWorld setBound(Rect bound) {
		mBound = bound;
		mGroundTop = mBound.top + mBound.height() * 4 / 5;
		return this;
	}

	public BirdWorld setSkySkin(Bitmap skySkin) {
		mSkySkin = skySkin;
		return this;
	}
	
	public BirdWorld setTreeSkin(Bitmap treeSkin) {
		tree = treeSkin;
		return this;
	}
	
	public BirdWorld setNestSkin(Bitmap nestSkin) {
		birdnest = nestSkin;
		return this;
	}

	public BirdWorld setGroundSkin(Bitmap groundSkin) {
		mGroundSkin = groundSkin;
		return this;
	}

	public BirdWorld setPipesSkin(Bitmap[] skins) {
		mPipesSkin = skins;
		return this;
	}

	public BirdWorld setRollingSpeed(int rollingSpeed) {
		mRollingSpeed = rollingSpeed;
		return this;
	}

	// 自定义水管模版，需要就从队列里取
	private void genTemplatePipeList() {
		int top = mBound.top + mBound.height() / 10;
		int space = mBound.height() * 3 / 10;
		int step = mBound.height() / 10;
		mTemplatePipeList.clear();
		while (top + space < mGroundTop) {
			PipePair pp = new PipePair().setDownBottom(top).setUpTop(
					top + space);
			mTemplatePipeList.add(pp);
			top += step;
		}
	}

	private void genPipePair() {
		PipePair pp = null;
		if (mTemplatePipeList.isEmpty()) {
			genTemplatePipeList();
		}
		PipePair temp = mTemplatePipeList
				.get((int) (Math.random() * mTemplatePipeList.size()));
		if (!mPipePairQueue.isEmpty()) {
			// peek是拿到，但是不从队列取出
			PipePair tmp = mPipePairQueue.peek();
			if (tmp.bound.right < 0) {
				// pool是从队列里取出，回收减少内存消耗
				pp = mPipePairQueue.poll();
				pp.setDownBottom(temp.downBottom).setUpTop(temp.upTop);
				pp.bound.set(temp.bound);
			}
		}
		if (pp == null) {
			pp = (PipePair) temp.clone();
		}
		// offer是放入队列
		mPipePairQueue.offer(pp);
	}

	public void makeStandby() {
		mIsStandby = true;
		mFrameCount = 0;
	}

	public boolean mIsDeadOnce() {
		return mIsDeadOnce;
	}

	public boolean IsStandBy() {
		return mIsStandby;
	}

	public void roll() {
		mIsStandby = false;
		mNextPipeFrameCount = -1;
	}

	public void draw(Canvas canvas) {
		int skyLeft = mBound.left;
		int groundLeft = mBound.left;
		if (!mIsStandby) {
			int recycleFrameCount = mBound.width() / mRollingSpeed;
			int groundFrameCount = mFrameCount % recycleFrameCount;
			skyLeft -= mFrameCount * mRollingSpeed / SPEED_SCALE;
			groundLeft -= groundFrameCount * mRollingSpeed;

			canvas.drawBitmap(mSkySkin, skyLeft + mBound.width(), mBound.top,
					null);
			canvas.drawBitmap(mGroundSkin, groundLeft + mBound.width(),
					mGroundTop, null);

			canvas.drawBitmap(mSkySkin, skyLeft, mBound.top, null);
			canvas.drawBitmap(mGroundSkin, groundLeft, mGroundTop, null);
			
			if(score<100){
				canvas.drawBitmap(tree, movingtree-=30,
						120, null);
				canvas.drawBitmap(birdnest, movingnest-=30,
						850, null);
			}
			
			// 槲寄生
			if (!dead) {
				score++;
			} else {
				mIsStandby = true;
				mIsDeadOnce = true;
				score=0;
				// recycleFrameCount=0;
				// groundFrameCount=0;
				// skyLeft=0;
				// groundLeft = 0;
			}
			for (PipePair pp : mPipePairQueue) {
				pp.draw(canvas);
			}

			Log.d("mytag", "mNextPipeFrameCount = " + mNextPipeFrameCount);
			if (mNextPipeFrameCount == -1) {
				mNextPipeFrameCount = recycleFrameCount;
			}

			if (mFrameCount == mNextPipeFrameCount) {
				genPipePair();
				mNextPipeFrameCount += recycleFrameCount / 1.5; // 2
				if (mNextPipeFrameCount >= (SPEED_SCALE * recycleFrameCount)) {
					mNextPipeFrameCount -= (SPEED_SCALE * recycleFrameCount);
				}
			}

			mFrameCount++;
			for (PipePair pp : mPipePairQueue) {
				pp.roll();
			}

			if (mFrameCount == (SPEED_SCALE * recycleFrameCount)) {
				mFrameCount = 0;
			}
		} else {
			canvas.drawBitmap(mSkySkin, skyLeft, mBound.top, null);
			canvas.drawBitmap(mGroundSkin, groundLeft, mGroundTop, null);

			// 槲寄生
			for (PipePair pp : mPipePairQueue) {
				pp.draw(canvas);
			}
		}
	}

	private class PipePair implements Cloneable {
		Rect bound;
		private int downBottom;
		private int upTop;

		PipePair() {
			bound = new Rect(mBound.right, mBound.top, mBound.right
					+ mPipesSkin[0].getWidth(), mGroundTop);
		}

		@Override
		protected Object clone() {
			PipePair pp = null;
			try {
				pp = (PipePair) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			pp.bound = new Rect(bound);
			return pp;
		}

		PipePair setDownBottom(int downBottom) {
			this.downBottom = downBottom;
			return this;
		}

		PipePair setUpTop(int upTop) {
			this.upTop = upTop;
			return this;
		}

		void roll() {
			bound.offset(-mRollingSpeed, 0);
		}

		void draw(Canvas canvas) {
			canvas.save();
			canvas.clipRect(bound);
			canvas.drawBitmap(mPipesSkin[0], bound.left, downBottom
					- mPipesSkin[0].getHeight(), null);
			canvas.drawBitmap(mPipesSkin[1], bound.left, upTop, null);
			canvas.restore();
		}
	}

	// 槲寄生
	public boolean ifDead() {
		int cot = 0;
		if (mBird.getBound().bottom - mGroundTop > DEFAULT_FLY_DISTANCE) {
			dead = true;
		} else {
			for (PipePair pp : mPipePairQueue) {
				cot++;
				if (pp.bound.left > 0
						&& cot >= 1
						&& (mBird.getBound().right - pp.bound.left > DEFAULT_FLY_DISTANCE || mBird.getBound().left - pp.bound.right > -DEFAULT_FLY_DISTANCE)
						&& mBird.getBound().bottom - pp.upTop > DEFAULT_FLY_DISTANCE-30) {
					dead = true;
					cot = 0;
					break;
				} else if (pp.bound.left > 0
						&& cot >= 1
						&& (mBird.getBound().right - pp.bound.left > DEFAULT_FLY_DISTANCE || mBird.getBound().left - pp.bound.right > -DEFAULT_FLY_DISTANCE)
						&& pp.downBottom - mBird.getBound().top > DEFAULT_FLY_DISTANCE-30) {
					dead = true;
					cot = 0;
					break;
				} else
					break;
			}
		}
		return dead;
	}
}
