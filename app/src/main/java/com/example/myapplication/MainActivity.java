package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;


    private static final String TAG = "MainActivity";

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "opencv not load");
        } else {
            Log.d(TAG, "opencv loaded: ");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        Button btn = findViewById(R.id.doaction);

        Button slc = findViewById(R.id.selection);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!OpenCVLoader.initDebug()) {

                    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, getApplicationContext(), baseLoaderCallback2);
                } else
                    baseLoaderCallback2.onManagerConnected(LoaderCallbackInterface.SUCCESS);

            }
        });


        slc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!OpenCVLoader.initDebug()) {

                    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, getApplicationContext(), baseLoaderCallback1);
                } else
                    baseLoaderCallback1.onManagerConnected(LoaderCallbackInterface.SUCCESS);

            }
        });


        Log.e("", "onCreate: image id" + R.id.image);

    }

    /**
     *
     */


    BaseLoaderCallback baseLoaderCallback1;

    {
        baseLoaderCallback1 = new BaseLoaderCallback(this) {

        };
    }

        BaseLoaderCallback baseLoaderCallback2;

        {
            baseLoaderCallback2 = new BaseLoaderCallback(this) {
                @Override
                public void onManagerConnected(int status) {
                    super.onManagerConnected(status);
                    if (status == LoaderCallbackInterface.SUCCESS) {
                        Mat newImage;

                        List<Mat> liste = new ArrayList<Mat>(3);
                        Mat Greenchannel;
                        TextView t1 = findViewById(R.id.green);
                        TextView t2 = findViewById(R.id.distance3c);

                        TextView t4 = findViewById(R.id.cup);
                        TextView moyenne = findViewById(R.id.moyenne);
                        TextView t6 = findViewById(R.id.temp);




                        try {


                            Mat sampleMat = new Mat(1, 4, CvType.CV_32F);

                            newImage = Utils.loadResource(getApplicationContext(), R.drawable.mob13);
                            Imgproc.cvtColor(newImage, newImage, Imgproc.COLOR_RGB2BGR);
                            new Size();
                            Imgproc.resize(newImage, newImage, new Size(100, 100));
                            Core.split(newImage, liste);
                            Greenchannel = liste.get(1);

// CLAHE

                            CLAHE clahe = Imgproc.createCLAHE();
                            clahe.setClipLimit(6);
                            clahe.setTilesGridSize(new Size(8, 8));
                            clahe.apply(Greenchannel, Greenchannel);




                            Mat st_element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(13, 13));
 // blackhat
                            Imgproc.morphologyEx(Greenchannel, Greenchannel, Imgproc.MORPH_BLACKHAT, st_element);

//OtsuTHres
                            Imgproc.threshold(Greenchannel, Greenchannel, 10, 255, Imgproc.THRESH_OTSU);

// MEDIANBLUR
                            Imgproc.medianBlur(Greenchannel, Greenchannel, 3);
                            Mat copygreen = Greenchannel.clone();
                            Point centroid = new Point();
                            centroid.x = newImage.cols() / 2;
                            centroid.y = newImage.rows() / 2;
                            Imgproc.circle(copygreen, centroid, 2, new Scalar(0, 255, 0), 4                 //Thickness of the circle
                            );
 //centoid location
                            // 1/masks creation
                            //mask n1
                            ImageMasking img= new ImageMasking();
                            img.setMask(newImage,new Point(0, 0),new Point(centroid.x, centroid.y),new Point(0, newImage.rows() - 1));
                            Mat mask0= img.getMask();
                            Mat hide0 = new Mat(newImage.size(), CvType.CV_8UC3);
                            Core.bitwise_and(Greenchannel, mask0, hide0);


                            //mask 2

                            ImageMasking img2= new ImageMasking();
                            img.setMask(newImage,new Point(newImage.height(), newImage.width()),new Point(centroid.x, centroid.y),new Point(0, newImage.width()));
                            Mat mask1= img2.getMask();
                            Mat hide2 = new Mat(newImage.size(), CvType.CV_8UC3);
                            Core.bitwise_and(Greenchannel, mask1, hide2);


                            //mask3
                            ImageMasking img3= new ImageMasking();
                            img.setMask(newImage,new Point(newImage.rows(), 0),new Point(centroid.x, centroid.y),new Point(0, 0));
                            Mat mask3= img3.getMask();

                            Mat hide3 = new Mat(newImage.size(), CvType.CV_8UC3);
                            Core.bitwise_and(Greenchannel, mask3, hide3);


                            // mask zone T
                            ImageMasking img4= new ImageMasking();
                            img.setMask(newImage,new Point(newImage.rows(), 0),new Point(centroid.x, centroid.y),new Point(newImage.height(), newImage.width()));
                            Mat mask4= img4.getMask();

                            Mat hide4 = new Mat(newImage.size(), CvType.CV_8UC3);
                            Core.bitwise_and(Greenchannel, mask4, hide4);
//Centroid coordinates
                            Mat whitee_z1 = new Mat();
                            //sum pixels hide0(S)
                            Core.findNonZero(hide0, whitee_z1);
                            int sumAz1 = Core.countNonZero(hide0);
                            float sommedepixelsdeN = (float) sumAz1;
                            String sumN = Float.toString(sommedepixelsdeN);
                            Mat whitee_z2 = new Mat();
                            //sum pixels hide2()
                            Core.findNonZero(hide2, whitee_z2);
                            int sumAz2 = Core.countNonZero(hide2);
                            float sommedepixelsdeI = (float) sumAz2;
                            String sumI = Float.toString(sommedepixelsdeI);
                            Mat whitee_z3 = new Mat();
                            //sum pixels hide3()
                            Core.findNonZero(hide3, whitee_z3);
                            int sumAz3 = Core.countNonZero(hide3);
                            float sommedepixelsdeS = (float) sumAz3;
                            String sumS = Float.toString(sommedepixelsdeS);
                            List<Point> pts = new ArrayList<Point>();
                            MatOfPoint mop = new MatOfPoint(whitee_z1);
                            pts = mop.toList();

                            Double QXz1, QYz1, Xz1, Yz1;
                            QXz1 = 0.0;
                            QYz1 = 0.0;


                            for (int i = 0; i < pts.size(); i++) {
                                QXz1 = QXz1 + (pts.get((i)).x / sumAz1);
                                QYz1 = QYz1 + (pts.get((i)).y / sumAz1);
                            }
                            Point centroide0 = new Point();
                            centroide0.x = QXz1;
                            centroide0.y = QYz1;
                            Mat exemple = newImage.clone();
                            Imgproc.circle(copygreen, centroide0, 2, new Scalar(127, 0, 0), 6                 //Thickness of the circle
                            );
                            Imgproc.circle(exemple, centroide0, 2, new Scalar(255, 255, 255), 2                  //Thickness of the circle
                            );
                            List<Point> pts2 = new ArrayList<Point>();
                            MatOfPoint mop2 = new MatOfPoint(whitee_z2);
                            pts2 = mop2.toList();
                            Double QXz2, QYz2, Xz2, Yz2;
                            QXz2 = 0.0;
                            QYz2 = 0.0;

                            for (int i = 0; i < pts2.size(); i++) {
                                QXz2 = QXz2 + (pts2.get((i)).x / sumAz2);
                                QYz2 = QYz2 + (pts2.get((i)).y / sumAz2);
                            }
                            Point centroide2 = new Point();
                            centroide2.x = QXz2;
                            centroide2.y = QYz2;
                            Imgproc.circle(copygreen, centroide2, 2, new Scalar(127, 0, 0), 6                 //Thickness of the circle
                            );
                            Imgproc.circle(exemple, centroide2, 2, new Scalar(255, 255, 255), 2                  //Thickness of the circle
                            );
                            //centroide z3
                            List<Point> pts3 = new ArrayList<Point>();
                            MatOfPoint mop3 = new MatOfPoint(whitee_z3);
                            pts3 = mop3.toList();
                            Double QXz3, QYz3, Xz3, Yz3;
                            QXz3 = 0.0;
                            QYz3 = 0.0;
                            for (int i = 0; i < pts3.size(); i++) {
                                QXz3 = QXz3 + (pts3.get((i)).x / sumAz3);
                                QYz3 = QYz3 + (pts3.get((i)).y / sumAz3);
                            }
                            Point centroide3 = new Point();
                            centroide3.x = QXz3;
                            centroide3.y = QYz3;
                            Imgproc.circle(copygreen, centroide3, 2, new Scalar(127, 0, 0), 6                 //Thickness of the circle
                            );
                            Imgproc.circle(exemple, centroide3, 2, new Scalar(255, 255, 255), 2                  //Thickness of the circle
                            );

                            Mat need = exemple.clone();
                            Bitmap bm = Bitmap.createBitmap(need.width(), need.height(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(need, bm);
                            imageView.setImageBitmap(bm);
//features1
                            Double c0c2;
                            c0c2 = Math.sqrt(Math.pow((centroide0.y - centroide2.y), 2) + Math.pow((centroide0.x - centroide2.x), 2));
                            Double c0c3;
                            c0c3 = Math.sqrt(Math.pow((centroide0.y - centroide3.y), 2) + Math.pow((centroide0.x - centroide3.x), 2));
                            float disns = c0c3.floatValue();
                            float disni = c0c2.floatValue();
                            String distancaNS = Float.toString(disns);
                            String distanceNI = Float.toString(disni);
                               //t1.setText(distancaNS);
                              //t2.setText(distanceNI);

 // features2

                            Point borders = new Point(centroide3.x, 0);
                            Point borderi = new Point(centroide2.x, newImage.height());
                            Double limites = Math.sqrt(Math.pow((centroide3.y - borders.y), 2) + Math.pow((centroide3.x - borders.x), 2));
                            Double limitei = Math.sqrt(Math.pow((centroide2.y - borderi.y), 2) + Math.pow((centroide2.x - borderi.x), 2));
                            float disbori = limitei.floatValue();
                            float disbors = limites.floatValue();
                            String distancebs = Float.toString(disbors);
                            String distancebi = Float.toString(disbori);
   //Predict
                            sampleMat.put(1, 1, disni);
                            sampleMat.put(1, 2, disns);

                            sampleMat.put(1, 3, disbori);
                            sampleMat.put(1, 4, disbors);
                         ;
                            float test[] = new float[]{disni, disns, disbori, disbors};
                            sampleMat.put(0, 0, test);

                            Svm svm = new Svm();
                            svm.setSvm();


                            float reponse = svm.getSvm().predict(sampleMat);

                            if (reponse == 1) {


                                t6.setText("Healthy fundus image");
                            } else {


                                t6.setText("Glaucoma-affected fundus image ");
                            }







                        } catch (IOException e) {
                            Log.e("error", "onManagerConnected: " + e.getMessage());
                        }

                    }


                }
            };
        }



}