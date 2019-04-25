package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import moshimoshi.cyplay.com.doublenavigation.model.events.SellerLoadedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.AuthenticationPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerDashboardActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.PicturedBadge;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.PinView;
import moshimoshi.cyplay.com.doublenavigation.utils.ActivityHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import moshimoshi.cyplay.com.doublenavigation.view.AuthenticationView;
import moshimoshi.cyplay.com.doublenavigation.view.NotifyLostUserPasswordView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AUser;

/**
 * Created by romainlebouc on 20/04/16.
 */
public class SellerLogInFragment extends BaseFragment implements AuthenticationView {

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @Inject
    AuthenticationPresenter authenticationPresenter;

    @BindView(R.id.login_top)
    View loginTopView;

    @BindView(R.id.profile_view)
    PicturedBadge picturedBadge;

    @BindView(R.id.authentication_pin_code)
    PinView authenticationPincode;

    @BindView(R.id.authentication_loading_view)
    LoadingView authenticationLoadingView;

    @BindView(R.id.pin_code_instructions)
    TextView pinCodeInstructions;

    private Seller seller;

    private AlertDialog warningAlert;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateDesign();
        authenticationPresenter.setView(this);
        authenticationPincode.setPasscodeEntryListener(new PinView.PasscodeEntryListener() {

            @Override
            public void onPasscodeEntered(String passcode) {
                picturedBadge.setLoading(true);
                authenticationPresenter.getAccessToken(seller, authenticationPincode.getText().toString());
                clear("");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().getIntent() != null && getActivity().getIntent().hasExtra(IntentConstants.EXTRA_SELLER)) {
            seller = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(IntentConstants.EXTRA_SELLER));
            if (picturedBadge != null) {
                picturedBadge.setProfile(seller);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        authenticationLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        clear(this.getResources().getString(R.string.enter_your_pin));
        authenticationPincode.requestToShowKeyboard();

    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void updateDesign() {
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void showLoading() {

    }


    @Override
    public void onAccessTokenSuccess() {
        // Update seller context
        sellerContext.setSeller(seller);

        bus.post(new SellerLoadedEvent(seller));
        onGetSellerSuccess();
    }

    @Override
    public void onInvalidateTokenSuccess() {

    }

    @Override
    public void onInvalidateTokenError() {

    }

    @Override
    public void onGetSellerSuccess() {
        BaseActivity baseActivity = (BaseActivity) this.getActivity();
        if (baseActivity != null && this.isAdded() && !baseActivity.isDestroyed()) {
            PlayRetailApp.get(this.getContext()).registerScreenLogoutReceiver(baseActivity.sellerInteractor,
                    baseActivity.apiValue,
                    sellerContext,
                    customerContext);

            picturedBadge.setLoading(false);
            Intent i = new Intent(getContext(), SellerDashboardActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(SellerLogInFragment.this.getActivity(), picturedBadge, "sellerBadgeTransition");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().startActivity(i, options.toBundle());
            delayCloseActivity();
        }

    }

    @Override
    public void onPasswordChangedSuccess(PR_AUser user) {

    }

    @Override
    public void onPasswordChangedFailure(BaseException exception) {

    }

    private void delayCloseActivity() {
        // To prevent 'lag' animation
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity activity = getActivity();
                if (activity != null && !activity.isDestroyed()) {
                    activity.finish();
                }
            }
        }, 1000);
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        picturedBadge.setLoading(false);
        authenticationLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        clear("");
        if (ExceptionType.AUTHENTICATION == exceptionType) {
            pinCodeInstructions.setText(R.string.request_error_message_authentication);
        } else {
            pinCodeInstructions.setText(R.string.request_error_message_try_again);
        }
        pinCodeInstructions.setTextColor(Color.RED);
    }

    private void clear(String instructions) {
        authenticationPincode.clearText();
        pinCodeInstructions.setTextColor(Color.BLACK);
        if (instructions != null) {
            pinCodeInstructions.setText(instructions);
        }
    }

}
