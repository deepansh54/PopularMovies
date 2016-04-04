package com.deepansh.and.popularmovies.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MovieCover extends ImageView {
    public MovieCover(Context context) {
        super(context);
    }

    public MovieCover(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieCover(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MovieCover(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, measuredWidth * 277 / 185);
    }
}
