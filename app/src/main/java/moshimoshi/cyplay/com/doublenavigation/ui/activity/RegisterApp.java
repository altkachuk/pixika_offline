package moshimoshi.cyplay.com.doublenavigation.ui.activity;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.adyen.library.AdyenLibraryInterface;
import com.adyen.library.AppInfo;
import com.adyen.library.RegistrationCompleteListener;
import com.adyen.library.ServerMode;
import com.adyen.library.StaffMember;
import com.adyen.library.exceptions.NotYetRegisteredException;
import com.adyen.library.real.LibraryReal;

import moshimoshi.cyplay.com.doublenavigation.R;
import ninja.cyplay.com.apilibrary.utils.LogUtils;


public class RegisterApp extends Activity {

    private static final String BETA_MODE_FLAG = ":0xdeadbeef";
    private static final String DEV_MODE_FLAG = ":0xcafebabe";
    private static final String TEST_MODE_FLAG = ":0xfacefeed";

    private EditText merchantCodeText;
    private EditText userNameText;
    private EditText passwordText;

    private ProgressDialog pd;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.registerapp);
        merchantCodeText = (EditText) findViewById(R.id.merchant);
        userNameText = (EditText) findViewById(R.id.name);
        passwordText = (EditText) findViewById(R.id.password);

        ((TextView) findViewById(R.id.signup)).setText(Html.fromHtml("signup"));
        ((TextView) findViewById(R.id.signup)).setMovementMethod(LinkMovementMethod.getInstance());

        pd = new ProgressDialog(RegisterApp.this);
        pd.setCancelable(false);
        pd.setTitle("logging_in");
        pd.setMessage("logging_in_message");
        pd.setIndeterminate(true);
    }

    /**
     * The register button that contacts the server to obtain a token
     *
     * @param v
     */
    public void clickRegister(final View v) {
        String merchantCode = merchantCodeText.getText().toString();
        String userName = userNameText.getText().toString();
        // Strip spaces from password
        String password = passwordText.getText().toString();
        password = password.trim();

        AdyenLibraryInterface lib = null;
        try {
            lib = LibraryReal.getLib();
        } catch (NotYetRegisteredException e) {
            Log.e(LogUtils.generateTag(this), "", e);
        }

        lib.setServerMode(ServerMode.LIVE);
        if (userName.endsWith(TEST_MODE_FLAG)) {
            userName = userName.replace(TEST_MODE_FLAG, "");
            lib.setServerMode(ServerMode.TEST);
        } else if (userName.endsWith(BETA_MODE_FLAG)) {
            userName = userName.replace(BETA_MODE_FLAG, "");
            lib.setServerMode(ServerMode.BETA);
        } else if (userName.endsWith(DEV_MODE_FLAG)) {
            userName = userName.replace(DEV_MODE_FLAG, "");
            lib.setServerMode(ServerMode.DEV);
        }

        final String sUser = String.format("%s@Company.MerchantAccount.%s", userName, merchantCode);
        Log.d(LogUtils.generateTag(this), "concatenated username merchantcode " + sUser);

        pd.show();


        // hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passwordText.getWindowToken(), 0);

        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterApp.this);
        //do login
        try {
            lib.registerApp(merchantCode, userName, password, new RegistrationCompleteListener() {

                @Override
                public void onRegistrationComplete(final RegistrationCompleteListener.CompletedStatus completedStatus, AppInfo appInfo, final String message) {
                    pd.dismiss();
                    switch (completedStatus) {
                        case ERROR:
                            builder.setTitle("login_failed_title");
                            Log.i(LogUtils.generateTag(this), "Unable to log in: " + message);
                            builder.setMessage("login_failed_message");
                            builder.setNeutralButton("Ok", null);
                            builder.create().show();
                            break;
                        case ERROR_BADCREDENTIALS:
                            builder.setTitle("login_failed_title");
                            Log.i(LogUtils.generateTag(this), "Unable to log in: " + message);
                            builder.setMessage("login_error_badcredentials");
                            builder.setNeutralButton("Ok", null);
                            builder.create().show();
                            break;
                        case ERROR_NONETWORK:
                            builder.setTitle("login_failed_title");
                            Log.i(LogUtils.generateTag(this), "Unable to log in: " + message);
                            builder.setMessage("login_error_nonetwork");
                            builder.setNeutralButton("Ok", null);
                            builder.create().show();
                            break;
                        case ERROR_NOROUTE:
                            builder.setTitle("login_failed_title");
                            Log.i(LogUtils.generateTag(this), "Unable to log in: " + message);
                            builder.setMessage("login_error_noroute");
                            builder.setNeutralButton("Ok", null);
                            builder.create().show();
                            break;
                        case OK:
                            try {
                                AdyenLibraryInterface lib = LibraryReal.getLib();
                                List<StaffMember> staffMembers = lib.retrieveStaffMembers();
                                if(staffMembers.size() > 0) {
                                   // startActivity(new Intent(RegisterApp.this, RegisterStaffMember.class));
                                } else {
                                   // startActivity(new Intent(RegisterApp.this, Payment.class));
                                }
                            } catch(NotYetRegisteredException e) {
                                Log.e(LogUtils.generateTag(this), "", e);
                                //startActivity(new Intent(RegisterApp.this, Payment.class));
                            }
                            finish();
                            break;
                        default:
                            builder.setTitle("login_failed_title");
                            Log.i(LogUtils.generateTag(this), "Unable to log in: " + message);
                            builder.setMessage(message);
                            builder.setNeutralButton("Ok", null);
                            builder.create().show();
                            break;
                    }
                }
            });
        } catch (final Exception e) {
            Log.e(LogUtils.generateTag(this), "register app threw ", e);
            pd.dismiss();
            builder.setTitle("login_failed_title");
            Log.i(LogUtils.generateTag(this), "Unable to log in: " + "login_failed_message");
            builder.setMessage("login_failed_message");
            builder.setNeutralButton("Ok", null);
            builder.create().show();
        }

    }
}
