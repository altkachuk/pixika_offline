package moshimoshi.cyplay.com.doublenavigation.view;

import java.io.File;

/**
 * Created by damien on 13/05/15.
 */
public interface CropPictureView extends BaseView {

    void showLoading();

    void onPictureError();

    void onPictureCroped(File file);

    void onPictureUploaded();


}
