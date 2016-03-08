package com.doctorfinderapp.doctorfinder.SocialShare;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.R;

public class SharePictureActivity extends ActionBarActivity {

    private Button cameraButton;
    private Button sharePicture;
    private ImageView thumbnail;

    public Bitmap picture;
    public Uri pictureUri;

    private int PHOTO_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_picture);

        cameraButton = (Button)findViewById(R.id.share_picture_camera_button);
        sharePicture = (Button)findViewById(R.id.share_picture_share_button);
        thumbnail = (ImageView)findViewById(R.id.share_picture_thumbnail);

        setupEvents();
        setupActionBar();
    }

    private void setupEvents() {
        cameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //startActivity(intent);
                // ^^ this will get us to the Camera activity, however it won't return any info even if
                //    we take a picture, so then we'd need to manually find the picture and use something
                //    like a ContentProvider to make it accessible. MediaStore + startActivityForResult = magic :)

                startActivityForResult(intent, PHOTO_ID);
            }
        });

        sharePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (picture == null) {
                    makeToast("Take a valid picture first!");
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_STREAM, pictureUri);
                    startActivity(Intent.createChooser(intent, "Share picture with..."));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PHOTO_ID) {
            if (resultCode == RESULT_OK) {
                // ^^ we got a picture back, otherwise we'd get a RESULT_CANCELLED
                this.showPicture(intent);
            }
        }
    }

    private void showPicture(Intent intent) {
        Bundle intentExtras = intent.getExtras();
        picture = (Bitmap)intentExtras.get("data");
        pictureUri = intent.getData();

        if (picture != null) {
            thumbnail.setImageBitmap(picture);
            makeToast("Picture set successfully!");
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.app.ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void makeToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}