package com.shiji.png.pat.app.mvp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shiji.png.pat.app.domain.model.PrefInfo;
import com.shiji.png.pat.app.domain.persistence.LogoStore;
import com.shiji.png.pat.app.domain.persistence.PrefStore;
import com.shiji.png.pat.app.util.BitmapUtils;
import com.shiji.png.pat.model.LogoInfo;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author bruce.wu
 * @date 2018/9/3
 */
public class BaseActivity extends AppCompatActivity {

    private volatile Drawable currencyDrawable;

    protected void toast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void toast(Throwable tr) {
        Toast.makeText(this, tr.getMessage(), Toast.LENGTH_SHORT).show();
    }

    protected PrefInfo getAppPreferences() {
        return new PrefStore(getApplicationContext()).load();
    }

    protected void logoInto(@IdRes int resId) {
        LogoInfo logoInfo = new LogoStore(getApplicationContext()).load();
        if (logoInfo == null || TextUtils.isEmpty(logoInfo.getData())) {
            return;
        }

        PrefInfo prefInfo = getAppPreferences();
        if (prefInfo.getMerId() == null || prefInfo.getRvcId() == null) {
            return;
        }

        if (prefInfo.getMerId().equals(logoInfo.getMerId())
                && prefInfo.getRvcId().equals(logoInfo.getRvcId())) {
            Bitmap logo = BitmapUtils.fromBase64(logoInfo.getData());
            ((ImageView)findViewById(resId)).setImageBitmap(logo);
        }
    }

    protected void setAmountView(TextView view, BigDecimal amount) {
        view.setCompoundDrawables(getCurrencyDrawable(view.getTextSize()), null, null, null);
        view.setText(amount.toPlainString());
    }

    protected Currency getCurrency() {
        String currencyCode = getAppPreferences().getCurrencyCode();
        return Currency.getInstance(currencyCode);
    }

    private synchronized Drawable getCurrencyDrawable(float textSize) {
        if (currencyDrawable == null) {
            currencyDrawable = BitmapUtils.toDrawable(getCurrency(), textSize);
        }
        return currencyDrawable;
    }

}
