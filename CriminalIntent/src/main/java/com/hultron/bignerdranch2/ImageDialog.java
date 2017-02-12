package com.hultron.bignerdranch2;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageDialog extends DialogFragment {
    private ImageView mImageView;
    private static String imagePath;
    private static Context sContext;

    public static void getImage(String path, Context context) {
        imagePath = path;
        sContext = context;
    }

//    public ImageDialog(String path, Context context) {
//        imagePath = path;
//        sContext = context;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageView);
        Bitmap bitmap = PictureUtils.getScaledBitmap(imagePath, getActivity());
        mImageView.setImageBitmap(bitmap);
        return view;
    }
}
