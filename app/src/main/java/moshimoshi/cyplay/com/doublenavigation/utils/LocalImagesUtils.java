package moshimoshi.cyplay.com.doublenavigation.utils;

import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by anishosni on 18/06/15.
 */
public class LocalImagesUtils {

    public static File createImageFile(String name) throws IOException {
        // Create an image file last_name
        String imageFileName = "JPEG_" + name + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: ic_customer_offer for use with ACTION_VIEW intents
        //photoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public static File createCroppedImageFile(String name) throws IOException {
        // Create an image file last_name
        String imageFileName = "JPEG_" + name + "_CROPED";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: ic_customer_offer for use with ACTION_VIEW intents
        //photoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public static byte[] readFileToByteArray(File file) throws IOException {


        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
                Log.e(LocalImagesUtils.class.getName(),e.getMessage(),e);
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
                Log.e(LocalImagesUtils.class.getName(),e.getMessage(),e);
            }
        }
        return ous.toByteArray();
    }

}
