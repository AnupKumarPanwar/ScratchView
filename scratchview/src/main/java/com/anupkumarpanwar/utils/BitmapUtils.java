package com.anupkumarpanwar.utils;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

/**
 * Created by sharish on 15/09/16.
 */
public class BitmapUtils {

    /**
     * Finds the percentage of pixels that do are empty.
     *
     * @param bitmap input bitmap
     * @return a value between 0.0 to 1.0 . Note the method will return 0.0 if either of bitmaps are null nor of same size.
     */
    public static float getTransparentPixelPercent(Bitmap bitmap) {

        if (bitmap == null) {
            return 0f;
        }

        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getHeight() * bitmap.getRowBytes());
        bitmap.copyPixelsToBuffer(buffer);

        byte[] array = buffer.array();

        int len = array.length;
        int count = 0;

        for (byte b : array) {
            if (b == 0) {
                count++;
            }
        }

        return ((float) (count)) / len;
    }
}
