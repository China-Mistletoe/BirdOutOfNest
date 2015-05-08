package com.mistletoe.flappyrabbit.element;

import com.mistletoe.flappyrabbit.R;
import com.mistletoe.flappyrabbit.view.GameView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Bird {
    private static final int RISING_MAX_ANGLE = -30;
    private static final int FALLING_MAX_ANGLE = 90;
    private static final int MAX_RISE_SPEED_Y_STANDBY = -10;
    private static final int FALL_ACCEL_Y_STANDBY = 1;
    private static final int MAX_RISE_SPEED_Y = -80;
    private static final int FALL_ACCEL_Y = 20;

    //éÎ¼ÄÉúprivate->public
    public Rect mBound;

    private Bitmap[] mBirdsSkin;

    private Matrix mMatrix;
    private boolean mNeedScale;
    private float mScaleX;
    private float mScaleY;

    private boolean mIsStandby;
    private int mSpeedY;
    private int mAccelY;
    private float mAngularSpeed;

    private int mFrameCount;
    private float mRotationAngle;
   

    public synchronized Bird setBound(Rect bound) {
        mBound = bound;
        return this;
    }
    
    //éÎ¼ÄÉú
    public  Rect getBound(){
    	return mBound;
    }

    public Bird setMatrix(Matrix matrix) {
        mMatrix = matrix;
        return this;
    }

    public Bird setBirdsSkin(Bitmap[] skin) {
        this.mBirdsSkin = skin;
        int bitmapHeight = mBirdsSkin[0].getHeight();
        int bitmapWidth = mBirdsSkin[0].getWidth();
        if (bitmapHeight != mBound.height() ||
                bitmapWidth != mBound.width()) {
            mScaleX = mBound.width() / bitmapWidth;
            mScaleY = mBound.height() / bitmapHeight;
            mNeedScale = true;
        } else {
            mNeedScale = false;
        }
        return this;
    }

    public void makeStandby() {
        mIsStandby = true;
        mSpeedY = MAX_RISE_SPEED_Y_STANDBY;
        mAccelY = FALL_ACCEL_Y_STANDBY;
    }

    public synchronized void shot() {
        mIsStandby = false;
        mAccelY = FALL_ACCEL_Y;
        mSpeedY = MAX_RISE_SPEED_Y;
        calAngularSpeed(RISING_MAX_ANGLE);
    }

    private void calAngularSpeed(int toAngle) {
        int frameCount = 0;
        if (mSpeedY < 0) {
            frameCount = mSpeedY / (-mAccelY);
        } else {
            frameCount = 2 * MAX_RISE_SPEED_Y / (-FALL_ACCEL_Y);
        }
        mAngularSpeed = (toAngle - mRotationAngle) / frameCount;
    }

    public void draw(Canvas canvas) {
        Bitmap skin = mBirdsSkin[mFrameCount++ % mBirdsSkin.length];
        
        if (mFrameCount == mBirdsSkin.length)
            mFrameCount = 0;

        mMatrix.reset();
        if (mNeedScale) {
            mMatrix.preScale(mScaleX, mScaleY);
        }
        synchronized (this) {
            if (mIsStandby) {
                mMatrix.postTranslate(mBound.left, mBound.top);
                mBound.top += mSpeedY;
                if (mSpeedY == MAX_RISE_SPEED_Y_STANDBY) {
                    mAccelY = FALL_ACCEL_Y_STANDBY;
                } else if (mSpeedY == -MAX_RISE_SPEED_Y_STANDBY) {
                    mAccelY = -FALL_ACCEL_Y_STANDBY;
                }
                mSpeedY += mAccelY;
                Log.d("ctagp1", "mBound.top = " + mBound.top);//bird position
            } else
            {
                mMatrix.preRotate(mRotationAngle, mBound.width() / 2, mBound.height() / 2);
                
                mMatrix.postTranslate(mBound.left, mBound.top);
                mBound.offset(0, mSpeedY);
                mSpeedY += mAccelY;
                if (mSpeedY == mAccelY) {
                    calAngularSpeed(FALLING_MAX_ANGLE);
                }
                float angle = mRotationAngle + mAngularSpeed;
                if (angle >= RISING_MAX_ANGLE && angle <= FALLING_MAX_ANGLE) {
                    mRotationAngle = angle;
                }
            }
   
            Log.d("ctagp2", "mBound.top = " + mBound.top);//bird position
        }
        canvas.drawBitmap(skin, mMatrix, null);
    }
    
}
