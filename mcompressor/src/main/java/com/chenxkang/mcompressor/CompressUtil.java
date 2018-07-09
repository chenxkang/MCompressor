package com.chenxkang.mcompressor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author: chenxkang
 * time  : 2018/7/5
 * desc  : util about compress
 */
public class CompressUtil {

    public static File compressImage(File targetImage, int newWidth, int newHeight, CompressFormat format, int quality, int fileSize, CompressUnit unit, String resultPath) {
        if (!isCompressImage(targetImage, fileSize, unit)) {
            return targetImage;
        }

        return compressFile(targetImage, newWidth, newHeight, format, quality, resultPath);
    }

    public static File compressFile(File targetImage, int newWidth, int newHeight, CompressFormat format, int quality, String resultPath) {
        FileOutputStream stream = null;
        Bitmap bitmap = null;
        File file = new File(resultPath).getParentFile();
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            stream = new FileOutputStream(resultPath);
            bitmap = decodeSampleBitmapFromFile(targetImage, newWidth, newHeight);
            if (bitmap != null) {
                bitmap.compress(format, quality, stream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                bitmap.recycle();
            }
        }

        return new File(resultPath);
    }

    private static boolean isCompressImage(File targetImage, int fileSize, CompressUnit unit) {
        int imageSize = getImageSize(targetImage);
        if (imageSize == 0) {
            return false;
        }

        int maxImageSize = 0;
        if (unit.equals(CompressUnit.MB)) {
            maxImageSize = fileSize * 1024 * 1024;
        } else if (unit.equals(CompressUnit.KB)) {
            maxImageSize = fileSize * 1024;
        }
        return imageSize > maxImageSize;
    }

    public static Bitmap decodeSampleBitmapFromFile(File targetImage, int newWidth, int newHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(targetImage.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(targetImage.getAbsolutePath(), options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int newWidth, int newHeight) {
        int width = options.outWidth, height = options.outHeight;
        int sampleSize = 1;

        int temp;

        if ((width > height && newWidth < newHeight) || (width < height && newWidth > newHeight)) {
            temp = newHeight;
            newHeight = newWidth;
            newWidth = temp;
        }

        if (width > newWidth || height > newHeight) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;

            while ((halfWidth / sampleSize) >= newWidth && (halfHeight / sampleSize) >= newHeight) {
                sampleSize *= 2;
            }
        }

        return sampleSize;
    }

    private static int getImageSize(File file) {
        int size = 0;
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

        return size;
    }
}
