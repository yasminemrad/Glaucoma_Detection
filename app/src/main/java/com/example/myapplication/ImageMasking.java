package com.example.myapplication;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImageMasking {
    private Mat mask;

    public Mat getMask() {
        //mask n1
        return mask;
    }

    public void setMask(Mat newImage,Point pt1,Point pt2,Point pt3) {
        Point centroid = new Point();
        centroid.x = newImage.cols() / 2;
        centroid.y = newImage.rows() / 2;
        Mat mask0 = new Mat(newImage.size(), CvType.CV_8UC1, Scalar.all(0));
        List<Point> ROI_Vertices = new ArrayList<>();
        ROI_Vertices.add(pt1);
        ROI_Vertices.add(pt2);
        ROI_Vertices.add(pt3);
        MatOfPoint maskMat = new MatOfPoint();
        maskMat.fromList(ROI_Vertices);
        MatOfPoint2f NewMt = new MatOfPoint2f(maskMat.toArray());
        MatOfPoint2f ROI_Poly = new MatOfPoint2f();
        Imgproc.approxPolyDP(NewMt, ROI_Poly, 1.0, true);
        MatOfPoint approxf1 = new MatOfPoint();
        ROI_Poly.convertTo(approxf1, CvType.CV_32S);
        Imgproc.fillConvexPoly(mask0, approxf1, new Scalar(255), 8, 0);
        this.mask = mask0;
    }
}
