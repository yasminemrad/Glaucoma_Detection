package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class BaseLoaderCallback implements LoaderCallbackInterface {

    protected Context mAppContext;
    public BaseLoaderCallback(Context AppContext) {
        mAppContext = AppContext;
    }
    @Override
    public void onManagerConnected(int status) {
        ImageView imageView = null;

        if (status == LoaderCallbackInterface.SUCCESS) {
            Mat newImage;

            try {
                newImage = Utils.loadResource(mAppContext, R.drawable.drishtit12);
                Imgproc.cvtColor(newImage, newImage, Imgproc.COLOR_RGB2BGR);
                new Size();
                Mat need = newImage.clone();
                Bitmap bm = Bitmap.createBitmap(need.width(), need.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(need, bm);
                imageView.setImageBitmap(bm);

            } catch (IOException e) {
                Log.e("error", "onManagerConnected: " + e.getMessage());
            }

        }


    }

    @Override
    public void onPackageInstall(int operation, InstallCallbackInterface callback) {

    }
}
