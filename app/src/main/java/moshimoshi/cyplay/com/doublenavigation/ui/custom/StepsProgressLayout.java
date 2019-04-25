package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.Step;

/**
 * Created by romainlebouc on 08/03/2017.
 */

public class StepsProgressLayout extends LinearLayout {

    private int stepTabWidth;

    private int totalTabs;

    private int startLineLength;
    private float lineHeight = 4;
    private float circleRadius;
    private int moveX;
    private int currentStep = 0;

    private List<Integer> stepCompletedIconPos;

    private Paint mPaint;
    private Drawable checkedIcon;

    @BindView(R.id.steps_container)
    LinearLayout stepsContainer;

//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;

    private List<Step> steps;
    private List<StepView> stepTabList;
    private Context mContext;

    public StepsProgressLayout(Context context) {
        this(context, null);
    }

    public StepsProgressLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepsProgressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PlayRetailApp.get(context).inject(this);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_steps_progress, this, true);
        // Bind xml
        ButterKnife.bind(this);
        stepCompletedIconPos = new ArrayList<>();
        mContext = context;

        initCheckedIcon(context);
        initAttrs(attrs);
        initPaint();
    }

    private void initCheckedIcon(Context context) {
        checkedIcon = ContextCompat.getDrawable(context, R.drawable.ic_step_checked);
//        checkedIcon = DrawableCompat.wrap(checkedIcon);
//        DrawableCompat.setTint(checkedIcon.mutate(), ContextCompat.getColor(context, R.color.colorAccent));
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.StepsProgressLayout);

        circleRadius = ta.getDimension(R.styleable.StepsProgressLayout_circle_radius, 8);
        lineHeight = ta.getDimension(R.styleable.StepsProgressLayout_lien_height, 4);

        ta.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    public void setSteps(List<Step> steps) {
        this.stepsContainer.removeAllViews();
        this.steps = steps;
        this.stepTabList = new ArrayList<>();
        for (Step step : steps) {
            StepView stepView = new StepView(this.getContext());
            stepView.setStep(step);
            stepView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            stepTabList.add(stepView);
            stepsContainer.addView(stepView);
        }
        totalTabs = steps.size();
        stepsContainer.requestLayout();
    }

    public void setCurrentStep(Step currentStep) {
        int stepPos = 0;

        for (Step step : steps) {
            if (step.getId() == currentStep.getId()) {
                this.currentStep = stepPos;
                upDateTextColor();
                return;
            }
            stepPos++;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cCount = getChildCount();

        if (cCount == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            //measure child for calculate the total height for this tablayout
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            int cHeight;
            int height = 0;

            for (int i = 0; i < cCount; i++) {
                View childView = getChildAt(i);
                cHeight = childView.getMeasuredHeight();
                height = Math.max(height, cHeight);
            }
            //TotalHeight = TextView height + circle Radius
            setMeasuredDimension(widthMeasureSpec, (int) (height + circleRadius));
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (steps != null && steps.size() != 0) {
            stepTabWidth = w / totalTabs;
            startLineLength = stepTabWidth / 2;
            invalidate();
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();
        //draw reach lien
        canvas.translate(0, getHeight() - circleRadius);
        mPaint.setStrokeWidth(lineHeight);
        canvas.drawLine(0, 0, startLineLength + moveX, 0, mPaint);
        //draw unreach line
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mPaint.setAlpha(64);
        canvas.drawLine(startLineLength + moveX, 0, stepTabWidth * totalTabs, 0, mPaint);
        //draw circle
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mPaint.setStrokeWidth(0);
        mPaint.setAlpha(255);
        canvas.drawCircle(startLineLength + moveX, 0, circleRadius, mPaint);
        //draw step complete icon
        for (Integer position : stepCompletedIconPos) {
            checkedIcon.setBounds(position - (int) circleRadius, (int) -circleRadius, position + (int) circleRadius, (int) circleRadius);
            checkedIcon.draw(canvas);
//            canvas.drawCircle(position, 0, circleRadius, mPaint);
        }

        canvas.restore();

        super.dispatchDraw(canvas);
    }


    public void scroll(int position, float positionOffset) {
        moveX = (int) (stepTabWidth * (position + positionOffset));

        if (stepCompletedIconPos.size() < position) {
            stepCompletedIconPos.add(startLineLength + stepTabWidth * (position - 1));
        }

        invalidate();
    }


    private void upDateTextColor() {
        for (int index = 0; index < totalTabs; index++) {
            StepView view = stepTabList.get(index);
            if (index < currentStep) {
                view.text.setTextColor(ContextCompat.getColor(mContext, R.color.Black));
            } else if (index == currentStep) {
                view.text.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            } else {
                view.text.setTextColor(ContextCompat.getColor(mContext, R.color.DarkGray));
            }
        }

    }
}
