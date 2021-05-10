package com.example.myapplication;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.SVM;

import static org.opencv.ml.Ml.ROW_SAMPLE;

public class Svm {
private SVM svm;


    public SVM getSvm() {
        return svm;
    }

    public void setSvm() {
        float trainingData[] = {};
        int labels[]= {};
        Mat trainingDataMat = new Mat(60, 4, CvType.CV_32FC1);
        trainingDataMat.put(0, 0, trainingData);
        Mat labelsMat = new Mat(60, 1, CvType.CV_32SC1);
        labelsMat.put(0, 0, labels);
        //Train SVM
        SVM svm = SVM.create();
        svm.setType(SVM.C_SVC);
        svm.setKernel(SVM.RBF);
        svm.setC(1000);
        svm.setGamma(0.001);
        svm.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 100, 1e-6));
        svm.train(trainingDataMat, ROW_SAMPLE, labelsMat);
        this.svm = svm;
    }
}
