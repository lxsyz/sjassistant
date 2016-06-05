package com.example.administrator.sjassistant.util;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/6/3.
 */
public class BitmapCrop {

    public static Bitmap SquareCrop(Bitmap bitmap,boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        //int wh = w > h ? h : w;// 裁切后所取的正方形区域边长
        int wh = 200;
        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
                false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }


        return bmp;
    }
}
