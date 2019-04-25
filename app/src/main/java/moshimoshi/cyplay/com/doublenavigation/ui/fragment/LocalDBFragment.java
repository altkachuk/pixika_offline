package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.BuildConfig;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ImportActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SplashScreenActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;

/**
 * Created by damien on 25/04/16.
 */
public class LocalDBFragment extends BaseFragment {

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.state_loading_view)
    LoadingView stateLoadingView;

    @Nullable
    @BindView(R.id.background_image)
    ImageView backgroundImageView;

    @BindView(R.id.debug_text_view)
    TextView debugTextView;

    @BindView(R.id.version)
    TextView version;

    @BindView(R.id.load_button)
    Button loadButton;

    @BindView(R.id.debug_text)
    TextView debugText;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_db, container, false);
    }

    public void clearDebugText() {
        debugText.setText("");
    }

    public void addDebugText(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String s = debugText.getText().toString();
                String[] sarr = s.split("\n");
                if (sarr.length > 10) {
                    s = "";
                    for (int i = sarr.length - 10; i < sarr.length; i++) {
                        s += "\n" + sarr[i];
                    }
                }
                s += "\n" + text;
                debugText.setText(s);
            }
        });
    }

    public void showLoadButton() {
        loadButton.setVisibility(View.VISIBLE);
    }

    public void hideLoadButton() {
        loadButton.setVisibility(View.GONE);
    }

    public void setState(LoadingView.LoadingState state) {
        stateLoadingView.setLoadingState(state);
    }

    public void start() {
        goToActivity(SplashScreenActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // debug gesture
        initDebugGesture();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // image Backgroung
        if (backgroundImageView != null) {
            Picasso.get()
                    .load(R.drawable.splash)
                    .fit()
                    .centerInside()
                    .into(backgroundImageView);
        }
        version.setText(BuildConfig.CLIENT_NAME + "-" + BuildConfig.BUILD_TYPE + " " + BuildConfig.VERSION_NAME);

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initDebugGesture() {
        coordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();
            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);
                        if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap
                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                            break;
                        }

                        if (numberOfTaps > 0 && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }
                        lastTapTimeMs = System.currentTimeMillis();
                        if (numberOfTaps == 3) {
                            //handle triple tap
                            debugTextView.setVisibility(View.VISIBLE);
                        }
                }
                return true;
            }
        });
    }

    private void goToActivity(Class activityClass) {
        Intent intent = new Intent(getContext(), activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.reload_button)
    public void onReloadClick() {
        ;
    }

    @OnClick(R.id.load_button)
    public void onLoadDbButton() {
        loadButton.setVisibility(View.GONE);
        ((ImportActivity) getActivity()).load();
    }

}

