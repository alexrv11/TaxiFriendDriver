package com.taxi.friend.drivers.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.widget.ImageView;

import com.taxi.friend.drivers.TaxiGlobalInfo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoManager {

    public static Bitmap setPic(ImageView imageView, String photoPath) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

        return bitmap;
    }

    public static Bitmap rotateImage(Bitmap image, int orientation) {
        int degree = 0;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree= 90; break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270; break;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        return rotatedImg;
    }

    public static int getOrientationPhoto(String path) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (Exception e){
            e.printStackTrace();
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        return orientation;
    }

    public static File createImageFile(File storageDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir     /* directory */
        );

        return image;
    }
}
