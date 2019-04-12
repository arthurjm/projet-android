package com.package1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * @author Arthur
 * Work with the Google api vision com.google.android.gms.vision.face
 * Detect faces and draw sunglasses on these faces
 */
public class FaceDetection {

    private Bitmap sunglasses;
    private FaceDetector faceDetector;
    private Canvas canvas;

    /**
     * Constructor
     *
     * @param ctx Context
     */
    public FaceDetection(Context ctx) {
        sunglasses = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.sunglasses);

        faceDetector = new FaceDetector.Builder(ctx)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.NO_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
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
            drawSunglasses(face);
        }

        return tmpBmp;
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
