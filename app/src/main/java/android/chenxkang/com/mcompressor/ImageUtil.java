package android.chenxkang.com.mcompressor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * author: chenxkang
 * time  : 2018/7/6
 * desc  : util about image
 */
public class ImageUtil {

    public static String getPathFromUri(Context context, Uri uri) {
        Cursor cursor;

        String[] projection = {MediaStore.Images.Media.DATA};
        cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null)
            return null;

        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String result = cursor.getString(index);

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    public static String getImageSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (size >= 1024 * 1024) {
            return (size / (1024 * 1024)) + "MB";
        }

        if (size >= 1024) {
            return (size / 1024) + "KB";
        }

        return size + "B";
    }
}
