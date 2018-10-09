package com.chenxkang.mcompressor;

import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import java.io.File;

/**
 * author: chenxkang
 * time  : 2018/7/6
 * desc  : My Compressor
 */
public class MCompressor {

    private int maxWidth = 960;
    private int maxHeight = 540;
    private CompressFormat compressFormat;
    private int quality;
    private String fromPath, toPath;
    private int fileSize;
    private CompressUnit unit;

    public MCompressor() {
        this.compressFormat = CompressFormat.JPEG;
        this.quality = 100;
        this.fileSize = 1024;
        this.unit = CompressUnit.KB;
        this.toPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    public static MCompressor from() {
        return new MCompressor();
    }

    public MCompressor compressFormat(CompressFormat format) {
        this.compressFormat = format;
        return this;
    }

    public MCompressor greaterThan(int fileSize, CompressUnit unit) {
        this.fileSize = fileSize;
        this.unit = unit;
        return this;
    }

    public MCompressor quality(int quality) {
        this.quality = quality;
        return this;
    }

    public MCompressor fromFilePath(String path) {
        this.fromPath = path;
        return this;
    }

    public MCompressor toFilePath(String path) {
        this.toPath = path;
        return this;
    }

    public File compress() {
        return compressToFile(fromPath);
    }

    private File compressToFile(String path) {
        return compressToFile(new File(path));
    }

    private File compressToFile(File file) {
        return compressToFile(file, file.getName());
    }

    private File compressToFile(File file, String fileName) {
        return CompressUtil.compressImage(file, maxWidth, maxHeight, compressFormat, quality, fileSize, unit, toPath + File.separator + fileName);
    }

}
