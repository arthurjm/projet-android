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
 * Detect faces and draw sunglasses on these faces
 */
public class FaceDetection {

    private Bitmap sunglasses;
    private Bitmap eye;
    private Bitmap ear;
    private Bitmap nose;
    private FaceDetector faceDetector;
    private Canvas canvas;

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public FaceDetection(Context ctx) {
        sunglasses = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.sunglasses);
        eye = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.red);
        ear = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.blue);
        nose = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.green);

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

        // Draw sunglasses on each faces
        for (int i = 0; i < sparseArray.size(); i++) {
            Face face = sparseArray.valueAt(i);
            directLandmarks(face);
        }

        return tmpBmp;
    }

    private void directLandmarks(Face face) {
        for (Landmark landmark:face.getLandmarks()) {
            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);

            if (landmark.getType() == Landmark.LEFT_EYE || landmark.getType() == Landmark.RIGHT_EYE) {
                drawEye(cx, cy);
            }

            if (landmark.getType() == Landmark.LEFT_EAR || landmark.getType() == Landmark.RIGHT_EAR) {
                drawEar(cx, cy);
            }

            if (landmark.getType() == Landmark.NOSE_BASE) {
                drawNose(cx, cy);
            }
        }
    }

    /**
     * To trick the face with sunglasses
     *
     */
    private void drawEye(int cx, int cy) {
            /*float x1 = face.getPosition().x;
            float y1 = face.getPosition().y;
            float x2 = x1 + face.getWidth();
            float y2 = y1 + face.getHeight();
            RectF rectF = new RectF(x1, y1, x2, y2);

            canvas.drawBitmap(sunglasses, null, rectF, null);*/
            int scaleHeight = eye.getScaledHeight(canvas);
            canvas.drawBitmap(eye, cx - eye.getWidth()/2, cy - eye.getHeight()/2, null);
    }

    private void drawEar(int cx, int cy) {
        int scaleHeight = ear.getScaledHeight(canvas);
        canvas.drawBitmap(ear, cx - ear.getWidth()/2, cy - ear.getHeight()/2, null);
    }

    private void drawNose(int cx, int cy) {
        int scaleHeight = nose.getScaledHeight(canvas);
        canvas.drawBitmap(nose, cx - nose.getWidth()/2, cy - nose.getHeight()/2, null);
    }

    /**
     * To trick the face with sunglasses
     *
     * @param face The face to put the sunglasses
     */
    private void drawSunglasses(Face face) {
        float x1 = face.getPosition().x;
        float y1 = face.getPosition().y;
        float x2 = x1 + face.getWidth();
        float y2 = y1 + face.getHeight();
        RectF rectF = new RectF(x1, y1, x2, y2);

        canvas.drawBitmap(sunglasses, null, rectF, null);
    }
}
