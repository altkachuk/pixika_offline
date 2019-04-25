package atproj.cyplay.com.dblibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by andre on 23-Aug-18.
 */

public class FileUtil {

    static public List<File> getFilesInDownloadDir(Context context) {
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        return getFilesInDir(context, downloadDir);
    }

    static public List<File> getFilesInDir(Context context, File dir) {
        File[] files = dir.listFiles();
        List<File> fileList = new ArrayList<>(Arrays.asList(files));

        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return (int)(f1.lastModified() - f2.lastModified());
            }
        });

        return fileList;
    }

    static public boolean existsDirInAppDir(Context context, String subDir) {
        String appDir = context.getApplicationInfo().dataDir;
        File dir = new File(appDir, subDir);
        return dir.exists();
    }

    static public void writeFileToDownload(String data, String fileName) throws IOException {
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        writeFile(data, downloadDir, fileName);
    }

    static public void writeFile(String data, File dir, String fileName) throws IOException {
        if (!dir.exists())
            dir.mkdir();

        File file = new File(dir, fileName);
        if (!file.exists())
            file.createNewFile();

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(data);
        bw.close();
    }

    static public String readFileFromDownload(Context context, String fileName) {
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadDir, fileName);

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            Log.d("IOException Error", e.getMessage());
        }
        return "";
    }

    static public String readTextFileFromAssets(Context context, String fileName) {
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

    static public void deleteDirFromAppDir(Context context, String subDir) {
        String appDir = context.getApplicationInfo().dataDir;
        File dir = new File(appDir, subDir);
        deleteDir(dir);
    }

    static public void deleteDir(File dir) {
        if (dir.exists()) {
            dir.delete();
        }
    }

    static public void unzipToAppDir(Context context, File zipFile, String subDir) throws IOException {
        String appDir = context.getApplicationInfo().dataDir;
        File targetDir = new File(appDir, subDir);
        unzip(zipFile, targetDir);
    }

    static public void unzipToAppDir(Context context, Uri zipFile, String subDir) throws IOException {
        String dataDir = context.getApplicationInfo().dataDir;
        File targetDir = new File(dataDir, subDir);
        unzip(context, zipFile, targetDir);
    }

    static public boolean checkImageFileInAppDir(Context context, String toSubDir, String name) {
        String dataDir = context.getApplicationInfo().dataDir;
        File targetDir = new File(dataDir, toSubDir);
        File imageFile = new File(targetDir, name + ".jpg");

        return imageFile.exists();
    }

    static public void saveBitmapToAppDir(Context context, Bitmap bitmap, String toSubDir, String name) {
        String dataDir = context.getApplicationInfo().dataDir;
        File targetDir = new File(dataDir, toSubDir);

        if (!targetDir.exists()) {
            if (!targetDir.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                targetDir.mkdir();
            }
        }

        File fileName = new File(targetDir, name + ".jpg");


        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void unzipFromDownloadToAppDir(Context context, String fileName, String toSubDir)throws IOException {
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadDir, fileName);

        String dataDir = context.getApplicationInfo().dataDir;
        File targetDir = new File(dataDir, toSubDir);
        unzip(file, targetDir);
    }

    static public void unzip(File zipFile, File targetDir) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));

        try {
            ZipEntry zipEntry;
            int count;
            byte[] buffer = new byte[8192];
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File file = new File(targetDir, zipEntry.getName());
                File dir = zipEntry.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdir()) {
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }
                if (zipEntry.isDirectory())
                    continue;

                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zipInputStream.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zipInputStream.close();
        }
    }

    static public void unzip(Context context, Uri zipFile, File targetDir) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(zipFile);
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));

        try {
            ZipEntry zipEntry;
            int count;
            byte[] buffer = new byte[8192];
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File file = new File(targetDir, zipEntry.getName());
                File dir = zipEntry.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdir()) {
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }
                if (zipEntry.isDirectory())
                    continue;

                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zipInputStream.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zipInputStream.close();
        }
    }
}
