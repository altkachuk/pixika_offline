package atproj.cyplay.com.dblibrary.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by andre on 09-Aug-18.
 */

public class FileReader {

    static public final String readTextFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            Log.d("Exception Error", e.getMessage());
        };

        return "";
    }

    static public final String readTextFromAssets(Context context, String fileName) {
        BufferedReader reader = null;
        String result = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                result += mLine;
            }
        } catch (IOException e) {
            Log.d("IOException Error", e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d("IOException Error", e.getMessage());
                }
            }
        }

        return result;
    }
}
