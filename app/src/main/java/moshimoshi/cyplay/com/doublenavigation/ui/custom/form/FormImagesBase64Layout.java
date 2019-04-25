package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.CameraInitiator;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ICameraActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerImageAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ImageUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.MesuarementUtil;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.utils.ReflectionUtils;

/**
 * Created by damien on 07/01/16.
 */
public class FormImagesBase64Layout extends FormEventBusView implements CameraInitiator {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    @Inject
    CustomerContext customerContext;

    @Inject
    CustomerInteractor customerInteractor;

    @Nullable
    @BindView(R.id.image_list_recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.label_text_view)
    TextView labelTextView;

    @Nullable
    @BindView(R.id.is_required_text_view)
    TextView requiredTextView;

    private Activity activity;
    protected boolean isloaded = false;
    private List<Media> medias;
    private CustomerImageAdapter adapter;

    private File imageFile;

    private AlertDialog alertDialog;

    public FormImagesBase64Layout(Activity context) {
        super(context);
        this.activity = context;
        ((ICameraActivity) activity).setCameraInitiator(this);
        init(context);
    }

    public FormImagesBase64Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FormImagesBase64Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    protected void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.form_images_base64_layout, this);
        ButterKnife.bind(this);

        isloaded = true;

        adapter = new CustomerImageAdapter(this.getContext(), medias);
        setupRecyclerView();

        updateInfo();
    }

    private void setupRecyclerView() {
        int columns = 3;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), columns);
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Add Decorator
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(columns, MesuarementUtil.dpToPixel(getContext(), 6f), true));
        // OnClick action
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new FormImagesBase64Layout.MediaClick()));
    }

    protected void updateInfo() {
        if (isloaded && row != null) {
            // is Editable
            recyclerView.setEnabled(!FormHelper.isDisabled(descriptor));
            // Label
            String labelText = StringUtils.getStringResourceByName(getContext(), row.getLabel());
            labelTextView.setText(labelText);
            // isRequired
            requiredTextView.setVisibility((FormHelper.isRequired(descriptor) ? VISIBLE : GONE));
            // set default assignedValue
            List<Media> mediaDataProvider = new ArrayList<>();

            if (medias == null) {
                try {
                    medias = new ArrayList<>();
                    List<Media> value = ReflectionUtils.get(customer, row.getTag());
                    if (value != null) {
                        for (Media media : value) {
                            if (ImageUtils.checkValidCustomerMedia(media)) {
                                medias.add(media);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(FormImagesBase64Layout.class.getName(), e.getMessage(), e);
                }
            }
            for (Media media : medias) {
                mediaDataProvider.add(media);
            }
            mediaDataProvider.add(null);

            adapter.setMedias(mediaDataProvider);
        }
    }

    @Override
    public void setRow(PR_FormRow row) {
        super.setRow(row);
        updateInfo();
    }

    @Override
    public void imageRecieved() {
        //Media media = ImageUtils.createCustomerMedia(bitmap);
        Media media = ImageUtils.createCustomerMedia(imageFile);
        if (imageFile.exists()) imageFile.delete();
        medias.add(media);
        setValue(medias);
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------


    @Override
    public View runValidation() {
        if (row == null || descriptor == null)
            return null;
        if (customer != null)
            ReflectionUtils.set(customer, row.getTag(), getValue());
        return null;
    }

    @Override
    public void setValue(Object val) {
        List<Media> tempMedias = (List<Media>) val;
        medias = new ArrayList<>();
        for (Media media : tempMedias) {
            if (ImageUtils.checkValidCustomerMedia(media)) {
                medias.add(media);
            }
        }
        updateInfo();
    }

    @Override
    public List<Media> getValue() {
        return medias;
    }


    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------


    private class MediaClick implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(View view, final int position) {

            if (position < medias.size()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                alertDialog = builder.setTitle(R.string.delete)
                        .setMessage(R.string.want_to_remove_customer_badge)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                medias.remove(position);
                                updateInfo();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.show();
            } else {
                dispatchTakePictureIntent();
            }

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = createImageFile();
        Uri uri = FileProvider.getUriForFile(getContext(), getContext()
                .getPackageName() + ".provider", imageFile);
        //Uri uri = Uri.fromFile(imageFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile(){
        File mediaStorageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_"+ timeStamp + "_";
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", mediaStorageDir);
        } catch (IOException e) {};

        return image;
    }

}