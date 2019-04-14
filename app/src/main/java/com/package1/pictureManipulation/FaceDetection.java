package com.package1.pictureManipulation;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.package1.R;

/**
 * @author Arthur
 * Work with the Google api vision com.google.android.gms.vision.face
 * It detects faces on a picture and draws green eyes and clown nose on those faces
 */
public class FaceDetection {

    private Bitmap eye;
    private Bitmap nose;
    private FaceDetector faceDetector;
    private Canvas canvas;

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public FaceDetection(Context ctx) {

        eye = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.eye);
        nose = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.nose);

        faceDetector = new FaceDetector.Builder(ctx)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();
    }

    /**
     * Detect all the faces on a bitmap and draw sunglasses on each faces
     *
     * @param bmp Source bitmap
     * @return the image with the face tricked
     */
    public Bitmap drawOnImage(Bitmap bmp) {
        Bitmap tmpBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
        canvas = new Canvas(tmpBmp);
        canvas.drawBitmap(bmp, 0, 0, null);

        Frame frame = new Frame.Builder().setBitmap(bmp).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame); // Detect all the faces

        // Draw on each faces
        for (int i = 0; i < sparseArray.size(); i++) {
            Face face = sparseArray.valueAt(i);
            directLandmarks(face);
        }

        return tmpBmp;
    }

    /**
     * Browse all the face landmarks and call functions to draw new eyes and new nose
     * @param face The face to modify
     */
    private void directLandmarks(Face face) {
        for (Landmark landmark : face.getLandmarks()) {
            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);
            // scaleWidth and scaleHeight to scale the bitmaps to the face size
            int scaleWidth = eye.getScaledWidth((int) face.getWidth()/4);
            int scaleHeight = eye.getScaledHeight((int) face.getWidth()/4);

            if (landmark.getType() == Landmark.NOSE_BASE) {
                drawNose(cx, cy, scaleWidth, scaleHeight);
            }

            if (landmark.getType() == Landmark.LEFT_EYE || landmark.getType() == Landmark.RIGHT_EYE) {
                drawEye(cx, cy, scaleWidth, scaleHeight);
            }
        }
    }

    /**
     * Draw green eyes
     * @param cx Eye x coordinate
     * @param cy Eye y coordinate
     * @param scaleWidth scaleWidth for the scaled nose bitmap
     * @param scaleHeight scaleHeight for the scaled nose bitmap
     */
    private void drawEye(int cx, int cy, int scaleWidth, int scaleHeight) {
        Bitmap scaledEye = Bitmap.createScaledBitmap(eye, scaleWidth, scaleHeight, true);
        canvas.drawBitmap(scaledEye, cx - scaleWidth / 2, cy - scaleHeight / 2, null);
    }

    /**
     * Draw clown nose
     * @param cx Nose x coordinate
     * @param cy Nose y coordinate
     * @param scaleWidth scaleWidth for the scaled nose bitmap
     * @param scaleHeight scaleHeight for the scaled nose bitmap
     */
    private void drawNose(int cx, int cy, int scaleWidth, int scaleHeight) {
        Bitmap scaledNose = Bitmap.createScaledBitmap(nose, scaleWidth, scaleHeight, true);
        canvas.drawBitmap(scaledNose, cx - scaleWidth / 2, cy - scaleHeight / 2, null);
    }


}
