package com.package1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

public class FaceDetection {
    private Bitmap sunglass;
    private FaceDetector faceDetector;
    private Canvas canvas;

    public FaceDetection(Context ctx) {
        sunglass = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.sunglass);

        faceDetector = new FaceDetector.Builder(ctx)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
    }

    public Bitmap putSunglass(Bitmap bmp) {
        Bitmap tmpBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
        canvas = new Canvas(tmpBmp);
        canvas.drawBitmap(bmp, 0, 0, null);

        Frame frame = new Frame.Builder().setBitmap(bmp).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);

        for (int i = 0; i < sparseArray.size(); i++) {
            com.google.android.gms.vision.face.Face face = sparseArray.valueAt(i);
            directLandmarks(face);
        }

        return tmpBmp;
    }

    private void directLandmarks(Face face) {
        for (Landmark landmark:face.getLandmarks()) {
            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);

            drawOnImageView(landmark.getType(), cx, cy);
        }
    }

    private void drawOnImageView(int type, int cx, int cy) {
        if (type == Landmark.NOSE_BASE) {
            Log.i("test", "test");
            int scaleHeight = sunglass.getScaledHeight(canvas);
            canvas.drawBitmap(sunglass, cx, cy, null);
        }
    }
}
