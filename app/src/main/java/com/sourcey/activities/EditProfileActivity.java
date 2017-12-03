package com.sourcey.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.sourcey.models.BasicInfo;
import com.sourcey.utils.ImageUtils;
import com.sourcey.utils.PermissionUtils;


public class EditProfileActivity extends AppCompatActivity {
    public static final String KEY_BASICINFO = "basicInfo";
    public static final int REQ_CODE_PICK_IMAGE = 500;
    private BasicInfo basicInfo = new BasicInfo();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // enable the back button
         //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //basicInfo = getIntent().getParcelableExtra(KEY_BASICINFO);
        if (basicInfo != null) {
            setUpUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * if we get the iamge and return back to this activity
         * update the user picture using showImage
         * edit user picture step 3
         * */
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                showImage(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * if the permission is granted, pick image for external storage
         * */
        if (requestCode == PermissionUtils.REQ_CODE_WRITE_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    private void setUpUI() {

        ((EditText) findViewById(R.id.input_edit_firstName)).setText(basicInfo.firstName);
        ((EditText) findViewById(R.id.input_edit_lastName)).setText(basicInfo.lastName);
        ((EditText) findViewById(R.id.input_edit_address)).setText(basicInfo.address);
        ((EditText) findViewById(R.id.input_edit_email)).setText(basicInfo.email);
        ((EditText) findViewById(R.id.input_edit_password)).setText(basicInfo.password);
        /**
         * if we have the uri of the user picture, show the user picture
         * */
        if (basicInfo.imageUri != null) {
            showImage(basicInfo.imageUri);
        }
        /**
         * edit user picture step 1
         * detail explanation for upload an image
         * http://sudhanshuvinodgupta.blogspot.com/2012/07/using-intentactionpick.html
         * */
        findViewById(R.id.basic_info_edit_image_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * if do not have the permission for reading the external storage
                 * ask the user for the permission
                 * it will then go to "onRequestPermissionsResult"
                 * */
                Log.d("onclick","image");

                if (!PermissionUtils.checkPermission(EditProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionUtils.requestReadExternalStoragePermission(EditProfileActivity.this);
                } else { // we already have the storage, pick the image
                    pickImage();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.ic_save:
                saveAndExit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndExit() {
        if (basicInfo == null) {
            basicInfo = new BasicInfo();
        }
        ((EditText) findViewById(R.id.input_edit_firstName)).setText(basicInfo.firstName);
        ((EditText) findViewById(R.id.input_edit_lastName)).setText(basicInfo.lastName);
        ((EditText) findViewById(R.id.input_edit_address)).setText(basicInfo.address);
        ((EditText) findViewById(R.id.input_edit_email)).setText(basicInfo.email);
        ((EditText) findViewById(R.id.input_edit_password)).setText(basicInfo.password);
        basicInfo.imageUri = (Uri)(findViewById(R.id.info_edit_image)).getTag();

        // put the content into the intent
        Intent intent = new Intent();
        intent.putExtra(KEY_BASICINFO, basicInfo);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void showImage(Uri imageUri) {
        ImageView imageView = (ImageView) findViewById(R.id.info_edit_image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // a tag use to identify views
        // this tag is used for storing the image uri
        imageView.setTag(imageUri);
        ImageUtils.loadImage(this, imageUri, imageView);
    }

    /**
     * edit user picture step 2
     * pick the image from external storage
     * */
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"),
                REQ_CODE_PICK_IMAGE);
    }
}