package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by damien on 10/05/16.
 */
public class EmptyWithImageView extends LinearLayout {

    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.text_view)
    TextView textView;

    // Set from styleable references
    private String text;
    private int imageDrawable;

    public EmptyWithImageView(Context context) {
        super(context);
        init(context, null);
    }

    public EmptyWithImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EmptyWithImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_empty_with_image_view, this, true);
        // Bind xml
        ButterKnife.bind(this);
        // Get styleable
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.EmptyWithImageView);
            imageDrawable = array.getResourceId(R.styleable.EmptyWithImageView_image_ref, -1);
            text = getResources().getString(array.getResourceId(R.styleable.EmptyWithImageView_empty_text_string, -1));
            // Do this when done.
            array.recycle();

            // Set results from attrs
            imageView.setImageResource(imageDrawable);
            textView.setText(text);
        }
    }

}
