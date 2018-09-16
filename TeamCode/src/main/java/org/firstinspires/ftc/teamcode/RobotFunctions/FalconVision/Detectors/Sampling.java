package org.firstinspires.ftc.teamcode.RobotFunctions.FalconVision.Detectors;

import org.firstinspires.ftc.teamcode.RobotFunctions.FalconVision.OpenCVpipeline;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a opencv detector for detecting where the gold mineral is to sample
 */

public class Sampling extends OpenCVpipeline {//TODO: crashes when program is stopped, need to fix
    private Mat rgba = new Mat();
    private Mat hsv = new Mat();
    private Mat blurred = new Mat();
    private Mat filtered = new Mat();
    private List<MatOfPoint> contours = new ArrayList<>();

    private double contourThresh = 400;
    private int contourId;

    public enum position {
        left, center, right, unknown
    }

    public Mat processFrame(Mat rgba, Mat gray){
        this.rgba = rgba;
        Imgproc.cvtColor(rgba, hsv, Imgproc.COLOR_RGB2HSV);
        Imgproc.blur(hsv, blurred, new Size(5, 5));
        Core.inRange(blurred, new Scalar(20, 40, 200), new Scalar(40, 255, 255), filtered); //need to tune
        Imgproc.findContours(filtered, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        int i = 0;
        double maxArea = 0;
        double contourArea = 0;
        Rect boundingRect;
        while(opmode.opModeIsActive() && i < contours.size()){ //made for loop out of while loop because I need to check if opmode is still running
            MatOfPoint cnts = contours.get(i);
            contourArea = Imgproc.contourArea(cnts);

            if(contourArea > contourThresh){
                if(contourArea > maxArea){
                    maxArea = contourArea;
                    contourId = i;
                }
            }
            i++;
        }

        if(maxArea > 0){ //checks if there is a contour to draw
            Imgproc.drawContours(rgba, contours, contourId, new Scalar(0, 255, 0), 3);
            boundingRect = Imgproc.boundingRect(contours.get(contourId));
            Imgproc.rectangle(rgba, boundingRect.tl(), boundingRect.br(), new Scalar(0, 0, 255), 3);
            opmode.telemetry.addData("contour id", contours.get(contourId).size());
        }



        opmode.telemetry.update();

        contours.clear();

        return rgba;

    }
}
