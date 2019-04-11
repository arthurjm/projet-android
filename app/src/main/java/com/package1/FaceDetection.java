package com.package1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

/**
 * @author Arthur
 * To detect and trick a face
 */
public class FaceDetection {
    private Bitmap sunglass;
    private Bitmap hat;
    private Bitmap beard;
    private FaceDetector faceDetector;
    private Canvas canvas;

    /**
     * Constructor
     *
     * @param ctx
     */
    public FaceDetection(Context ctx) {
        sunglass = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.sunglass);
        hat = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.hat);
        beard = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.beard);

        faceDetector = new FaceDetector.Builder(ctx)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
    }

    /**
     * To detect and trick the face
     *
     * @param bmp
     * @return the image with the face tricked
     */
    public Bitmap putSunglass(Bitmap bmp) {
        Bitmap tmpBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
        canvas = new Canvas(tmpBmp);
        canvas.drawBitmap(bmp, 0, 0, null);


        Frame frame = new Frame.Builder().setBitmap(bmp).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);

        for (int i = 0; i < sparseArray.size(); i++) {
            Face face = sparseArray.valueAt(i);
            Log.i("FACE", "Nb visages " + i + 1);
            directLandmarks(face);
        }

        return tmpBmp;
    }

    /**
     * To detect the face in the picture
     *
     * @param face
     */
    private void directLandmarks(Face face) {
        for (Landmark landmark : face.getLandmarks()) {
            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);

            drawOnImageView(landmark.getType(), cx, cy, face);
        }
    }

    /**
     * To trick the face with sunglass hat and beard
     *
     * @param type
     * @param cx
     * @param cy
     * @param face
     */
    private void drawOnImageView(int type, int cx, int cy, Face face) {
        if (type == Landmark.NOSE_BASE) {
            Paint rectPaint = new Paint();
            rectPaint.setStrokeWidth(5);
            rectPaint.setColor(Color.RED);
            rectPaint.setStyle(Paint.Style.STROKE);

            float x1 = face.getPosition().x;
            float y1 = face.getPosition().y;
            float x2 = x1 + face.getWidth();
            float y2 = y1 + face.getHeight();
            RectF rectF = new RectF(x1, y1, x2, y2);
            Log.i("FACE", "Face x1:" + x1 + " x2:" + x2 + " y1:" + y1 + " y2:" + y2);
            //canvas.drawRoundRect(rectF, 2, 2, rectPaint);

            Log.i("FACE", "Nose position x:" + cx + " y:" + cy);

            int scaleWidth = sunglass.getScaledWidth(canvas);
            int scaleHeight = sunglass.getScaledHeight(canvas);
            Log.i("FACE", "Scale width:" + scaleWidth + " height:" + scaleHeight);
            //canvas.drawBitmap(sunglass, cx - (scaleWidth/2), cy - scaleHeight, null);
            canvas.drawBitmap(sunglass, null, rectF, null);
            canvas.drawBitmap(hat, null, new RectF(x1, y1 - face.getHeight() / 2, x2, y1 + face.getHeight() / 2), null);
            canvas.drawBitmap(beard, null, new RectF(x1, y2 - face.getHeight() / 2, x2, y2 + face.getHeight() / 2), null);
        }
    }
}
