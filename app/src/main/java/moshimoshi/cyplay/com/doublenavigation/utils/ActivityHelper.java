package moshimoshi.cyplay.com.doublenavigation.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerDashboardActivity;
import ninja.cyplay.com.apilibrary.models.singleton.ClientApplication;

public class ActivityHelper {

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showKeyboard(View view, Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public static void goToSellerDashboard() {

        Application application = ClientApplication.getInstance().getApplication();
        if (application != null) {
            Intent loginIntent = new Intent(application.getApplicationContext(), SellerDashboardActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(loginIntent);
        }
    }

    public static void goToCustomerPage() {

        Application application = ClientApplication.getInstance().getApplication();
        if (application != null) {
            Intent loginIntent = new Intent(application.getApplicationContext(), CustomerActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(loginIntent);
        }
    }

}