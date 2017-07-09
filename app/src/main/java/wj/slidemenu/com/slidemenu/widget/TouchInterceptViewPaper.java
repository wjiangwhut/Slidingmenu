/*
 * Copyright (C) 2015 Get Remark
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wj.slidemenu.com.slidemenu.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jiangwei on 16/6/21.
 */
public class TouchInterceptViewPaper extends ViewPager {

    private Context mContext;

    private boolean mDisableSroll = false;
    public TouchInterceptViewPaper(Context context) {
        super(context);
        this.mContext = context;
    }

    public TouchInterceptViewPaper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }


    public void setDisableScroll(boolean bDisable) {
        mDisableSroll = bDisable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDisableSroll) {
            return false;
        }
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDisableSroll){
            return false;
        }
        try {
            return super.onTouchEvent(event);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}