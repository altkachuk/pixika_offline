package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;

import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_EDIT_ID;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_EDIT_TEXT;

/**
 * Created by romainlebouc on 04/01/2017.
 */

public class FullScreenEditTextActivity extends BaseActivity {

    @BindView(R.id.main_edit_text)
    EditText mainEditText;

    private String id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_edit_text);
        mainEditText.setText(this.getIntent().getStringExtra(EXTRA_EDIT_TEXT));
        id = this.getIntent().getStringExtra(EXTRA_EDIT_ID);

    }

    @Override
    protected boolean isLeftCrossClosable() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_full_screen_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_validate_text:
                Intent data = new Intent();
                data.putExtra(EXTRA_EDIT_TEXT,mainEditText.getText().toString());
                data.putExtra(EXTRA_EDIT_ID,id);
                this.setResult(RESULT_OK,data);
                this.supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
