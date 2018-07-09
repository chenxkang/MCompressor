package android.chenxkang.com.mcompressor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenxkang.mcompressor.MCompressor;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBeforeIv, mAfterIv;
    private TextView mBeforeTv, mAfterTv;
    private Button mBeforeBtn, mAfterBtn;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initListener() {
        mBeforeBtn.setOnClickListener(this);
        mAfterBtn.setOnClickListener(this);
    }

    private void initView() {
        mBeforeIv = findViewById(R.id.main_before_compress_iv);
        mAfterIv = findViewById(R.id.main_after_compress_iv);
        mBeforeTv = findViewById(R.id.main_before_compress_tv);
        mAfterTv = findViewById(R.id.main_after_compress_tv);
        mBeforeBtn = findViewById(R.id.main_before_compress_btn);
        mAfterBtn = findViewById(R.id.main_after_compress_btn);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_before_compress_btn:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                        return;
                    }
                }

                Matisse.from(MainActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(1)
                        .gridExpectedSize(300)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(1000);

                break;
            case R.id.main_after_compress_btn:
                File file = MCompressor.from()
                        .fromFilePath(path)
                        .compress();

                if (file != null) {
                    Picasso.with(MainActivity.this).load(file).into(mAfterIv);
                    mAfterTv.setText(ImageUtil.getImageSize(file));
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Matisse.from(MainActivity.this)
                    .choose(MimeType.allOf())
                    .countable(true)
                    .maxSelectable(1)
                    .gridExpectedSize(300)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(new PicassoEngine())
                    .forResult(1000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            List<Uri> pictures = Matisse.obtainResult(data);
            if (pictures != null && pictures.size() > 0) {
                path = ImageUtil.getPathFromUri(MainActivity.this, pictures.get(0));
                if (!TextUtils.isEmpty(path)) {
                    File file = new File(path);
                    Picasso.with(MainActivity.this).load(file).into(mBeforeIv);
                    mBeforeTv.setText(ImageUtil.getImageSize(file));
                }
            }
        }
    }
}
