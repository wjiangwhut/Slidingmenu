package wj.slidemenu.com.slidemenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

import wj.slidemenu.com.slidemenu.R;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;
import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * Created by jiangwei on 17/6/24.
 */

public class SlidingMenu extends RelativeLayout implements View.OnClickListener{
    private RelativeLayout mMenu;
    private RelativeLayout mMenuSecond;
    private RelativeLayout mThumbnailContent;
    private RelativeLayout mFullscreenContent;

    private int mMenuWidth;
    private int mThumbnailWidth;
    private int mThumbnailMarginLeft;
    private int mThumbnailMarginRight;
    private int mScreenWidth;

    private TouchInterceptViewPaper mViewPager;

    private int mActivePointerId = INVALID_POINTER_ID;
    private float mPrevX;
    private float mPrevY;
    private float mCurrentX;
    private float mCurrentY;
    private float mScaleRationX;
    private boolean mMoved;

    private ArrayList<OnOpenListener> mOpenListener = new ArrayList<>();
    private ArrayList<OnCloseListener> mCloseListener = new ArrayList<>();
    private ArrayList<OnOpenedListener> mOpenedListener = new ArrayList<>();
    private ArrayList<OnClosedListener> mClosedListener = new ArrayList<>();

    private static final long MENU_OPEN_ANIMATION_DURING = 2000;
    private static final long MENU_CHANGE_ANIMATION_DURING = 250;

    private boolean mDisallowAll = false;

    public SlidingMenu(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenuView, defStyleAttr, 0);
        try {
            mMenuWidth = typedArray.getDimensionPixelSize(R.styleable.SlidingMenuView_menu_width,
                    context.getResources().getDimensionPixelSize(R.dimen.sliding_menu_width));
            mThumbnailMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SlidingMenuView_thumbnail_marginLeft,
                    context.getResources().getDimensionPixelSize(R.dimen.sliding_thumbnail_marginLeft));
            mThumbnailMarginRight = typedArray.getDimensionPixelSize(R.styleable.SlidingMenuView_thumbnail_marginRight,
                    context.getResources().getDimensionPixelSize(R.dimen.sliding_thumbnail_marginRight));
        } finally {
            typedArray.recycle();
        }

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mThumbnailWidth = mScreenWidth - (mMenuWidth + mThumbnailMarginLeft + mThumbnailMarginRight);
        mScaleRationX = (float) mThumbnailWidth / mScreenWidth;

        LayoutInflater.from(context).inflate(R.layout.sliding_menu, this, true);
        mMenu = (RelativeLayout)findViewById(R.id.menu);
        mMenuSecond = (RelativeLayout)findViewById(R.id.menu_second);
        mFullscreenContent = (RelativeLayout)findViewById(R.id.fullscreen_content);
        mThumbnailContent = (RelativeLayout)findViewById(R.id.thumbnail_content);

        ViewGroup.LayoutParams menuLP = mMenu.getLayoutParams();
        menuLP.width = mMenuWidth;
        mMenu.setLayoutParams(menuLP);

        ViewGroup.LayoutParams menuSecondLP = mMenuSecond.getLayoutParams();
        menuSecondLP.width = mMenuWidth;
        mMenuSecond.setLayoutParams(menuSecondLP);

        MarginLayoutParams thumbnailContentLP = (MarginLayoutParams) mThumbnailContent.getLayoutParams();
        thumbnailContentLP.width = mThumbnailWidth;
        thumbnailContentLP.height = (int)(mScaleRationX * getResources().getDisplayMetrics().heightPixels);
        thumbnailContentLP.rightMargin = mThumbnailMarginRight;
        mThumbnailContent.setLayoutParams(thumbnailContentLP);

        mMenu.setOnClickListener(this);
        mMenuSecond.setOnClickListener(this);
        mThumbnailContent.setOnClickListener(this);

        mMenu.setVisibility(VISIBLE);
        mThumbnailContent.setVisibility(VISIBLE);
        mMenuSecond.setVisibility(GONE);
        mFullscreenContent.setVisibility(GONE);

        ViewHelper.setPivotX(mFullscreenContent, getResources().getDisplayMetrics().widthPixels);
        ViewHelper.setPivotY(mFullscreenContent, getResources().getDisplayMetrics().heightPixels / 2);
        ViewHelper.setScaleX(mFullscreenContent, mScaleRationX);
        ViewHelper.setScaleY(mFullscreenContent, mScaleRationX);
        ViewHelper.setAlpha(mFullscreenContent, 0);
    }

    private void setViewProperty(float deltaX) {
        mMenu.setVisibility(View.VISIBLE);
        mThumbnailContent.setVisibility(View.VISIBLE);

        float scale = (mScreenWidth - deltaX) / mScreenWidth;
        float scaleX = scale >= mScaleRationX ? scale : mScaleRationX;
        float translationX = -mMenu.getMeasuredWidth() + deltaX;

        ViewHelper.setScaleX(mFullscreenContent, scaleX);
        ViewHelper.setScaleY(mFullscreenContent, scaleX);
        ViewHelper.setTranslationX(mMenu, translationX <=0 ? translationX : 0);
        ViewHelper.setAlpha(mThumbnailContent, 1 - scale);
        ViewHelper.setAlpha(mFullscreenContent, scale);

        if (mViewPager != null) {
            mViewPager.setDisableScroll(true);
        }

        if (!mMoved) {
            for (OnOpenListener onOpenListener : mOpenListener) {
                onOpenListener.onOpen();
            }
            mMoved = true;
        }
    }

    private void reset() {
        mActivePointerId = INVALID_POINTER_ID;
        mMoved = false;
        if (mViewPager != null) {
            mViewPager.setDisableScroll(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                switchMenu(true);
                break;
            case R.id.menu_second:
                switchMenu(false);
                break;
            case R.id.thumbnail_content:
                if (mMenu.getVisibility() == GONE) {
                    switchMenu(false);
                } else {
                    changeScene(false, mScreenWidth);
                }
                break;
        }
    }

    public void switchMenu(boolean switchToSecond) {
        mMenu.setVisibility(View.VISIBLE);
        mMenuSecond.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        if (switchToSecond) {
            set.playSequentially(
                    ObjectAnimator.ofFloat(mMenu, "scaleX", 1, 0),
                    ObjectAnimator.ofFloat(mMenuSecond, "scaleX", 0, 1)
            );
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mDisallowAll = true;
                    ViewHelper.setScaleX(mMenu, 1);
                    ViewHelper.setScaleX(mMenuSecond, 0);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDisallowAll = false;
                    mMenu.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mDisallowAll = false;
                    mMenu.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            set.playSequentially(
                    ObjectAnimator.ofFloat(mMenuSecond, "scaleX", 1, 0),
                    ObjectAnimator.ofFloat(mMenu, "scaleX", 0, 1)
            );
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mDisallowAll = true;
                    ViewHelper.setScaleX(mMenu, 0);
                    ViewHelper.setScaleX(mMenuSecond, 1);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDisallowAll = false;
                    mMenuSecond.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mDisallowAll = false;
                    mMenuSecond.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        set.setDuration(MENU_CHANGE_ANIMATION_DURING).start();
    }

    private void changeScene(boolean toThumbnail, final float deltaX) {
        float currentRatio = (mScreenWidth - deltaX) / mScreenWidth;
        float scaleX = currentRatio >= mScaleRationX ? currentRatio : mScaleRationX;

        if (toThumbnail) {
            float translationX = -mMenuWidth + (currentRatio == 1 ? 0 : deltaX);
            mMenu.setVisibility(View.VISIBLE);
            mThumbnailContent.setVisibility(View.VISIBLE);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(mFullscreenContent, "scaleX", scaleX, mScaleRationX),
                    ObjectAnimator.ofFloat(mFullscreenContent, "scaleY", scaleX, mScaleRationX),
                    ObjectAnimator.ofFloat(mThumbnailContent, "alpha", 1 - currentRatio, 1),
                    ObjectAnimator.ofFloat(mMenu, "translationX", translationX <= 0 ? translationX : 0, 0),
                    ObjectAnimator.ofFloat(mFullscreenContent, "alpha", currentRatio, 0)
            );
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mDisallowAll = true;
                    if (deltaX == 0) {
                        for (OnOpenListener onOpenListener : mOpenListener) {
                            onOpenListener.onOpen();
                        }
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDisallowAll = false;
                    resetView(mFullscreenContent);
                    mFullscreenContent.setVisibility(View.GONE);
                    for (OnOpenedListener onOpenedListener : mOpenedListener) {
                        onOpenedListener.onOpened();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mDisallowAll = false;
                    resetView(mFullscreenContent);
                    mFullscreenContent.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.setDuration((int)(MENU_OPEN_ANIMATION_DURING * currentRatio)).start();
        } else {
            float translationX = currentRatio == 0 ? 0 : (-mMenuWidth + deltaX);
            mFullscreenContent.setVisibility(View.VISIBLE);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(mFullscreenContent, "scaleX", scaleX, 1),
                    ObjectAnimator.ofFloat(mFullscreenContent, "scaleY", scaleX, 1),
                    ObjectAnimator.ofFloat(mFullscreenContent, "alpha", currentRatio, 1),
                    ObjectAnimator.ofFloat(mMenu, "translationX", translationX , -mMenu.getMeasuredWidth()),
                    ObjectAnimator.ofFloat(mThumbnailContent, "alpha", 1 - currentRatio, 0)
            );
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mDisallowAll = true;
                    if (deltaX == mScreenWidth) {
                        for (OnCloseListener onCloseListener : mCloseListener) {
                            onCloseListener.onClose();
                        }
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDisallowAll = false;
                    resetView(mThumbnailContent);
                    mMenu.setVisibility(View.GONE);
                    mThumbnailContent.setVisibility(View.GONE);
                    for (OnClosedListener onClosedListener : mClosedListener) {
                        onClosedListener.onClosed();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mDisallowAll = false;
                    resetView(mThumbnailContent);
                    mMenu.setVisibility(View.GONE);
                    mThumbnailContent.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.setDuration((int)(MENU_OPEN_ANIMATION_DURING * (1 - currentRatio))).start();
        }
    }

    protected void resetView(View view) {
        ViewHelper.setAlpha(view, 1);
        ViewHelper.setScaleX(view, 1);
    }


    /**
     * menu callback
     */
    public interface OnOpenListener {
        void onOpen();
    }

    public interface OnOpenedListener {
        void onOpened();
    }

    public interface OnCloseListener {
        void onClose();
    }

    public interface OnClosedListener {
        void onClosed();
    }

    public void addOnOpenListener(OnOpenListener listener) {
        mOpenListener.add(listener);
    }

    public void addOnCloseListener(OnCloseListener listener) {
        mCloseListener.add(listener);
    }

    public void addOnOpenedListener(OnOpenedListener listener) {
        mOpenedListener.add(listener);
    }

    public void addOnClosedListener(OnClosedListener listener) {
        mClosedListener.add(listener);
    }

    public void removeOnOpenListener(OnOpenListener listener) {
        mOpenListener.remove(listener);
    }

    public void removeOnCloseListener(OnCloseListener listener) {
        mCloseListener.remove(listener);
    }

    public void removeOnOpenedListener(OnOpenedListener listener) {
        mOpenedListener.remove(listener);
    }

    public void removeOnClosedListener(OnClosedListener listener) {
        mClosedListener.remove(listener);
    }

    public void setViewPager(TouchInterceptViewPaper viewPager) {
        mViewPager = viewPager;
    }

    public void showMenu(boolean animation) {
        if (animation) {
            changeScene(true, 0);
        } else {
            for (OnOpenListener onOpenListener : mOpenListener) {
                onOpenListener.onOpen();
            }
            ViewHelper.setTranslationX(mMenu, 0);
            mMenu.setVisibility(VISIBLE);
            mThumbnailContent.setVisibility(VISIBLE);
            mMenuSecond.setVisibility(GONE);
            mFullscreenContent.setVisibility(GONE);
            for (OnOpenedListener onOpenedListener : mOpenedListener) {
                onOpenedListener.onOpened();
            }
        }
    }

    public void showContent() {
        mFullscreenContent.setVisibility(VISIBLE);
        mMenu.setVisibility(GONE);
        mThumbnailContent.setVisibility(INVISIBLE);
        mMenuSecond.setVisibility(GONE);
        ViewHelper.setScaleX(mFullscreenContent, 1);
        ViewHelper.setScaleY(mFullscreenContent, 1);
        ViewHelper.setAlpha(mFullscreenContent, 1);
    }

    public void showContent(boolean animation) {
        if (mMenu.getVisibility() == GONE) {
            switchMenu(false);
            return;
        }
        if (animation) {
            changeScene(false, mScreenWidth);
        } else {
            for (OnCloseListener onCloseListener : mCloseListener) {
                onCloseListener.onClose();
            }
            showContent();
            for (OnClosedListener onClosedListener : mClosedListener) {
                onClosedListener.onClosed();
            }
        }
    }

    public void setCustomMenu(int resID) {
        LayoutInflater.from(getContext()).inflate(resID, mMenu, true);
    }

    public void setCustomMenuSecond(int resID) {
        LayoutInflater.from(getContext()).inflate(resID, mMenuSecond, true);
    }

    public void setCustomThumbnailContent(int resID) {
        LayoutInflater.from(getContext()).inflate(resID, mThumbnailContent, true);
    }

    public void setCustomFullscreenContent(int resID) {
        LayoutInflater.from(getContext()).inflate(resID, mFullscreenContent, true);
    }

    private boolean fullScreenEvent = false;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mDisallowAll) {
            return false;
        }
        int action = event.getAction() & MotionEventCompat.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            if (mFullscreenContent.getScaleX() == 1 && mFullscreenContent.getVisibility() == VISIBLE) {
                int index = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                if (mActivePointerId == INVALID_POINTER) {
                    return super.onInterceptTouchEvent(event);
                }
                mPrevX = event.getX();
                mPrevY = event.getY();
                fullScreenEvent = true;
                if (mViewPager != null) {
                    mViewPager.setDisableScroll(true);
                }
            } else {
                fullScreenEvent = false;
            }
        }

        if (action == MotionEvent.ACTION_MOVE && fullScreenEvent) {
            int pointerIndex = event.findPointerIndex(mActivePointerId);
            if (pointerIndex != -1) {
                mCurrentX = event.getX(pointerIndex);
                mCurrentY = event.getY(pointerIndex);
                float deltaX = mCurrentX - mPrevX;
                float deltaY = mCurrentY - mPrevY;

                if (Math.abs(deltaX) - Math.abs(deltaY) > 0 && deltaX > 0) {
                    return true;
                } else {
                if (mViewPager != null) {
                    mViewPager.setDisableScroll(false);
                }
            }
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDisallowAll) {
            return false;
        }
        if (mFullscreenContent.getVisibility() == VISIBLE) {
            int action = event.getAction();
            switch (action & event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    ViewHelper.setTranslationX(mMenu, -mMenu.getMeasuredWidth());
                    mPrevX = event.getX();
                    mPrevY = event.getY();
                    mActivePointerId = event.getPointerId(0);
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    // Find the index of the active pointer and fetch its position.
                    int pointerIndex = event.findPointerIndex(mActivePointerId);
                    if (pointerIndex != -1) {
                        mCurrentX = event.getX(pointerIndex);
                        mCurrentY = event.getY(pointerIndex);

                        float deltaX = mCurrentX - mPrevX;
                        float deltaY = mCurrentY - mPrevY;

                        if (Math.abs(deltaX) - Math.abs(deltaY) > 0 && deltaX > 0) {
                            if (deltaX >= mMenuWidth / 2) {
                                changeScene(true, mMenuWidth / 2);
                                reset();
                                return false;
                            } else {
                                setViewProperty(deltaX);
                            }
                        }
                    }
                    break;
                }

                case MotionEvent.ACTION_CANCEL:
                    reset();
                    break;

                case MotionEvent.ACTION_UP:
                    if (mMoved) {
                        float deltaX = mCurrentX - mPrevX;
                        if (deltaX > 0) {
                            if (deltaX >= mMenuWidth / 4) {
                                changeScene(true, deltaX);
                            } else {
                                changeScene(false, deltaX);
                            }
                        } else {
                            changeScene(false, 10);
                        }
                    }
                    reset();
                    break;

                case MotionEvent.ACTION_POINTER_UP: {
                    // Extract the index of the pointer that left the touch sensor.
                    int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    int pointerId = event.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mPrevX = event.getRawX();
                        mPrevY = event.getRawY();
                        mActivePointerId = event.getPointerId(newPointerIndex);
                    }
                    mMoved = false;
                    break;
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    public boolean isDisallowAll() {
        return mDisallowAll;
    }

    public boolean isMenuShowing() {
        return mMenu.getVisibility() == VISIBLE || mMenuSecond.getVisibility() == VISIBLE;
    }

    public void setDisallowAll(boolean disallowAll) {
        mDisallowAll = disallowAll;
    }

    public int getThumbnailWidth() {
        return mThumbnailWidth;
    }

    public float getScaleRationX() {
        return mScaleRationX;
    }
}
