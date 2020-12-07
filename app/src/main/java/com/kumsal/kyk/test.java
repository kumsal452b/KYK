package com.kumsal.kyk;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivImage;
    private Button btnSelectImage;

    /*Dialog camera*/
    private Dialog mChoosingDialog;
    private ImageView ivbChooseClose;
    private LinearLayout llCamera, llGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = (ImageView) findViewById(R.id.ivImage);
        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);

        dialog_choosing();

        btnSelectImage.setOnClickListener(this);
    }

    private void dialog_choosing() {
        mChoosingDialog = new Dialog(MainActivity.this, R.style.AppTheme);
        mChoosingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        mChoosingDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        mChoosingDialog.getWindow().setGravity(Gravity.BOTTOM);
        mChoosingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mChoosingDialog.setCancelable(true);
        mChoosingDialog.setContentView(R.layout.dialog_choose_cameragallery);

        ivbChooseClose = (ImageView) mChoosingDialog.findViewById(R.id.ivbChooseClose);
        llCamera = (LinearLayout) mChoosingDialog.findViewById(R.id.llCamera);
        llGallery = (LinearLayout) mChoosingDialog.findViewById(R.id.llGallery);

        llCamera.setOnClickListener(this);
        llGallery.setOnClickListener(this);
        ivbChooseClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectImage:
                mChoosingDialog.show();
                break;
            case R.id.ivbChooseClose:
                mChoosingDialog.dismiss();
                break;
            case R.id.llCamera:
                mChoosingDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions()) {
                        openCamera();
                        mChoosingDialog.dismiss();
                    }
                } else {
                    openCamera();
                    mChoosingDialog.dismiss();
                }
                break;
            case R.id.llGallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                        mChoosingDialog.dismiss();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 401);
                        mChoosingDialog.dismiss();
                    }
                } else {
                    openGallery();
                    mChoosingDialog.dismiss();
                }
                break;
        }
    }


    private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        int permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 402);
            return false;
        }
        return true;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 501);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 502);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 501 && resultCode == RESULT_OK && data != null) {
            Uri selectedURI = data.getData();

            try {
                Bitmap bitmap = convert_UriToBitmap(selectedURI);
                ivImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

//            CropImage.activity(selectedURI).start(this);

        } else if (requestCode == 502 && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            ivImage.setImageBitmap(mphoto);
           /* Uri selectedURI = getImageUri(mphoto);

            CropImage.activity(selectedURI).start(this);*/

//            ImageLoad.onBitmapLoadCirlce(DrawerActivity.this, mphoto, civProfilePic);
        } /*else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri selectedURI = result.getUri();

                try {
                    Bitmap bitmap = convert_UriToBitmap(selectedURI);
                    ivImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 401) {
            if (grantResults.length == 0 || grantResults == null) {
//                Logger.e(TAG, "Null Every thing");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            }
        } else if (requestCode == 402) {
            if (grantResults.length == 0 || grantResults == null) {
//                Logger.e(TAG, "Null Every thing");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            }
        }
    }

    private Bitmap convert_UriToBitmap(Uri selectedURI) throws IOException {
        return (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), selectedURI);
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (bitmap != null) {
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            return Uri.parse(path);
        } else {
            return Uri.parse("");
        }
    }

    @Override
    public void onBackPressed() {
        if (mChoosingDialog.isShowing()) {
            mChoosingDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mChoosingDialog.isShowing()) {
            mChoosingDialog.dismiss();
        }
        super.onDestroy();

    }
}