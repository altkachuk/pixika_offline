package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * Created by wentongwang on 23/05/2017.
 */

public class GestureImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener{

    private ScaleGestureDetector mScaleGestureDetector = null;

    private ScaleGestureListener listener = null;

    public GestureImageView(Context context) {
        this(context, null);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();

        if (scaleFactor > 1.0f) {
            if (listener != null) {
                listener.openGesture();
            }
        }

        return listener != null;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {

        return listener != null;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    public interface ScaleGestureListener {
        void openGesture();
    }

    public void setOnScaleGestureListener(ScaleGestureListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event);
    }
}
