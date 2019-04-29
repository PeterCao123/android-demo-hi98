package com.shiji.png.pinpad.model;

import android.content.Context;
import android.util.DisplayMetrics;

import com.uuzuche.lib_zxing.DisplayUtil;

/**
 * @author Falcon.cao
 * @date 2019/4/17
 */
public class ConfigUtils {
    public static void initDisplayOpinion(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(context, dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(context, dm.heightPixels);
    }
}
