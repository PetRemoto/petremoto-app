/**
 * Copyright (C) 2014 Embedded Systems and Pervasive Computing Lab - UFCG All
 * rights reserved.
 */

package com.petremoto.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.petremoto.R;

/**
 * The Class CircleImageView.
 */
public final class CircleImageView extends ImageView {

    /** The Constant HALF. */
    private static final float HALF = 0.5f;

    /** The Constant SCALE_TYPE. */
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    /** The Constant BITMAP_CONFIG. */
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    /** The Constant COLORDRAWABLE_DIMENSION. */
    private static final int COLORDRAWABLE_DIMENSION = 1;

    /** The Constant DEFAULT_BORDER_WIDTH. */
    private static final int DEFAULT_BORDER_WIDTH = 0;

    /** The Constant DEFAULT_BORDER_COLOR. */
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    /** The m drawable rect. */
    private final RectF mDrawableRect = new RectF();

    /** The m border rect. */
    private final RectF mBorderRect = new RectF();

    /** The m shader matrix. */
    private final Matrix mShaderMatrix = new Matrix();

    /** The m bitmap paint. */
    private final Paint mBitmapPaint = new Paint();

    /** The m border paint. */
    private final Paint mBorderPaint = new Paint();

    /** The m border color. */
    private int mBorderColor = DEFAULT_BORDER_COLOR;

    /** The m border width. */
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    /** The m bitmap. */
    private Bitmap mBitmap;

    /** The m bitmap shader. */
    private BitmapShader mBitmapShader;

    /** The m bitmap width. */
    private int mBitmapWidth;

    /** The m bitmap height. */
    private int mBitmapHeight;

    /** The m drawable radius. */
    private float mDrawableRadius;

    /** The m border radius. */
    private float mBorderRadius;

    /** The m ready. */
    private boolean mReady;

    /** The m setup pending. */
    private boolean mSetupPending;

    /**
     * Instantiates a new circle image view.
     *
     * @param context the context
     */
    public CircleImageView(final Context context) {
        super(context);
    }

    /**
     * Instantiates a new circle image view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public CircleImageView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new circle image view.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public CircleImageView(final Context context, final AttributeSet attrs,
            final int defStyle) {
        super(context, attrs, defStyle);
        super.setScaleType(SCALE_TYPE);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CircleImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(
                R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color,
                DEFAULT_BORDER_COLOR);

        a.recycle();

        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    /*
     * (non-Javadoc)
     * @see android.widget.ImageView#getScaleType()
     */
    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    /*
     * (non-Javadoc)
     * @see
     * android.widget.ImageView#setScaleType(android.widget.ImageView.ScaleType)
     */
    @Override
    public void setScaleType(final ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format(
                    "ScaleType %s not supported.", scaleType));
        }
    }

    /*
     * (non-Javadoc)
     * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(final Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        canvas.drawCircle(getWidth() / (float) 2, getHeight() / (float) 2,
                mDrawableRadius, mBitmapPaint);
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / (float) 2, getHeight() / (float) 2,
                    mBorderRadius, mBorderPaint);
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onSizeChanged(int, int, int, int)
     */
    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw,
            final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    /**
     * Gets the border color.
     *
     * @return the border color
     */
    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * Sets the border color.
     *
     * @param borderColor the new border color
     */
    public void setBorderColor(final int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    /**
     * Gets the border width.
     *
     * @return the border width
     */
    public int getBorderWidth() {
        return mBorderWidth;
    }

    /**
     * Sets the border width.
     *
     * @param borderWidth the new border width
     */
    public void setBorderWidth(final int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    /*
     * (non-Javadoc)
     * @see android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)
     */
    @Override
    public void setImageBitmap(final Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    /*
     * (non-Javadoc)
     * @see android.widget.ImageView#setImageDrawable(android.graphics.drawable.
     * Drawable )
     */
    @Override
    public void setImageDrawable(final Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    /*
     * (non-Javadoc)
     * @see android.widget.ImageView#setImageResource(int)
     */
    @Override
    public void setImageResource(final int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    /**
     * Gets the bitmap from drawable.
     *
     * @param drawable the drawable
     * @return the bitmap from drawable
     */
    private Bitmap getBitmapFromDrawable(final Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
                        COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            final Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (final OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * Setup.
     */
    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2,
                (mBorderRect.width() - mBorderWidth) / 2);

        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width()
                - mBorderWidth, mBorderRect.height() - mBorderWidth);
        mDrawableRadius = Math.min(mDrawableRect.height() / 2,
                mDrawableRect.width() / 2);

        updateShaderMatrix();
        invalidate();
    }

    /**
     * Update shader matrix.
     */
    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
                * mBitmapHeight) {
            scale = mDrawableRect.height() / mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * HALF;
        } else {
            scale = mDrawableRect.width() / mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * HALF;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + HALF) + mBorderWidth,
                (int) (dy + HALF) + mBorderWidth);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

}
