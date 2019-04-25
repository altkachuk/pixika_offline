package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomNestedScrollView extends NestedScrollView {

    private int y;

    public CustomNestedScrollView(Context context) {
        super(context);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean dispatchHandlerScroll(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = (int) e.getY();
                startNestedScroll(2);
                break;
            case MotionEvent.ACTION_MOVE:
                int dY = y - ((int) e.getY());
                dispatchNestedPreScroll(0, dY, null, null);
                dispatchNestedScroll(0, 0, 0, dY, null);
                break;
            case MotionEvent.ACTION_UP:
                stopNestedScroll();
                break;
        }
        return true;
    }
}