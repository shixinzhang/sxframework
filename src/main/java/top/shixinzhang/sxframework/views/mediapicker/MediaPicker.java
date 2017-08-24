/*
 * Copyright (c) 2017. shixinzhang (shixinzhang2016@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.shixinzhang.sxframework.views.mediapicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;

import top.shixinzhang.sxframework.R;
import top.shixinzhang.utils.CheckUtils;


public class MediaPicker implements View.OnClickListener {


    /**
     * 媒体选择回调
     */
    interface OnMediaPickerListener {
        void onSelectedMediaChanged(String mediaUri);
    }

    public void setOnMediaPickerListener(OnMediaPickerListener listener) {
        this.mMediaPickerListener = listener;
    }

    private static final int REQUEST_CODE_CROP_PHOTO = 2000;
    private static final int REQUEST_CODE_PICK_IMAGE = 2001;
    private static final int REQUEST_CODE_TAKE_PHOTO = 2002;

    private Uri imageUri;
    private File tempFile;
    private boolean mCropImage;
    private Activity mActivity;
    private OnMediaPickerListener mMediaPickerListener;
    private PopupWindow mPopupWindow;

    public MediaPicker(@NonNull Fragment fragment) {
        this(fragment.getActivity());
    }

    public MediaPicker(Activity activity) {
        CheckUtils.checkNotNull(activity);
        this.mActivity = activity;
        this.tempFile = createTempFile();
    }

    public void showPicker(Context context, boolean isCropImage, View anchorView) {
        this.mCropImage = isCropImage;
        View popupView = View.inflate(context, R.layout.pop_chose_photo, null);

        popupView.findViewById(R.id.tv_camera).setOnClickListener(this);
        popupView.findViewById(R.id.tv_gallery).setOnClickListener(this);
        popupView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


//        mPopupWindow.showAsDropDown(mIvUserImage);
        mPopupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);
    }


    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                String path = MediaPath.getPath(mActivity, data.getData());
                if (!TextUtils.isEmpty(path)) {
                    Uri uri = Uri.fromFile(new File(path));
                    if (uri != null) {
                        cropImageUri(uri, 300, 300, REQUEST_CODE_CROP_PHOTO);
                    }
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                cropImageUri(imageUri, 300, 300, REQUEST_CODE_CROP_PHOTO);
                break;
            case REQUEST_CODE_CROP_PHOTO:
                String imagePath = tempFile.getAbsolutePath();

                if (mMediaPickerListener != null) {
                    mMediaPickerListener.onSelectedMediaChanged(imagePath);
                }
                break;
        }
    }

    @SuppressLint("InlinedApi")
    private void openSystemPickImage() {
        Intent photoPickerIntent = null;
        if (Build.VERSION.SDK_INT < 19) {
            photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }
        photoPickerIntent.setType("image/*");
        photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            mActivity.startActivityForResult(photoPickerIntent,
                    REQUEST_CODE_PICK_IMAGE);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    private void openSystemCamera() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dirPath = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(dirPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            try {
                mActivity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }

        } else {
            toast("当前没有内存卡");
        }
    }

    @NonNull
    private File createTempFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(Environment.getExternalStorageDirectory(), +System.currentTimeMillis() + "image.png");
        } else {
            return new File(mActivity.getFilesDir(), +System.currentTimeMillis() + "image.png");
        }
    }

    /**
     * 截取图像部分区域
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @return
     */
    private void cropImageUri(@NonNull Uri uri, int outputX, int outputY, int requestCode) {
        if (!mCropImage && mMediaPickerListener != null) {
            mMediaPickerListener.onSelectedMediaChanged(uri.getPath());
            return;
        }

        try {
            // android1.6以后只能传图库中图片
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            mActivity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException ex) {
            toast("设备上无法找到裁剪功能");
        }
    }


    @Override
    public void onClick(@NonNull View v) {
        int i = v.getId();
        if (i == R.id.tv_camera) {
            openSystemCamera();
            disMissPop();

        } else if (i == R.id.tv_gallery) {
            openSystemPickImage();
            disMissPop();

        } else if (i == R.id.tv_cancel) {
            disMissPop();
        }
    }

    private void disMissPop() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }


    private void toast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

}
