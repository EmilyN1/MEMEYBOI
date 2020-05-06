package com.example.memeyboi;

import android.graphics.Bitmap;
import android.view.View;

public class ScreenShot {
    public static Bitmap takesc(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }
    public static Bitmap scview(View v) {
        return takesc(v.getRootView());
    }
}
