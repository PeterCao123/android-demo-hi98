package com.shiji.png.pat.app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.util.Currency;

/**
 * @author bruce.wu
 * @date 2018/5/25
 */
public final class BitmapUtils {

    /**
     * convert text to a bitmap
     *
     * @param text text body
     * @param textSize text size
     * @return bitmap
     */
    private static Bitmap fromText(String text, float textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();

        int width = (int)paint.measureText(text);
        int height = fm.descent - fm.ascent;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, 0, fm.leading - fm.ascent, paint);
        canvas.save();

        return bitmap;
    }

    public static Drawable toDrawable(Currency currency, float textSize) {
        Bitmap bitmap = fromText(currency.getSymbol(), textSize);
        Drawable drawable = new BitmapDrawable(null, bitmap);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public static Bitmap fromBase64(String text) {
        byte[] data = Base64.decode(text, Base64.NO_WRAP);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
    }

    private BitmapUtils() {}

}
