package com.devtonix.amerricard.ui.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.view.View;

public class GifView extends View {
    private Movie gif;
    private long startTime;
    private OnGifFinishedListener onGifFinishedListener;

    public GifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setGif(@RawRes int gif, @Nullable OnGifFinishedListener onGifFinishedListener) {
        this.gif = Movie.decodeStream(getContext().getResources().openRawResource(gif));
        startTime = 0;

        this.onGifFinishedListener = onGifFinishedListener;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }

        final float scaleFactor = Math.min((float) getWidth() / gif.width(), (float) getHeight() / gif.height());

        final float centerX = ((float) getWidth() / scaleFactor - (float) gif.width()) / 2f;
        final float centerY = ((float) getHeight() / scaleFactor - (float) gif.height()) / 2f;

        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(centerX, centerY);

        int relativeTime = (int) (System.currentTimeMillis() - startTime);

        if (relativeTime > gif.duration()) {
            relativeTime = gif.duration();

            if (onGifFinishedListener != null) {
                onGifFinishedListener.onFinished();
            }

            return;
        }

        gif.setTime(relativeTime);
        gif.draw(canvas, 0, 0);

        invalidate();
    }

    public interface OnGifFinishedListener {
        void onFinished();
    }
}
