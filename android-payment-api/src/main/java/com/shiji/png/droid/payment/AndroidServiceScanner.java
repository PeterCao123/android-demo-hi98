package com.shiji.png.droid.payment;

import android.content.Context;

import com.shiji.png.droid.payment.rx.DexScanner;
import com.shiji.png.payment.ServiceScanner;
import com.shiji.png.payment.annotation.ServiceDef;

import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author bruce.wu
 * @since 2018/12/5 16:38
 */
class AndroidServiceScanner implements ServiceScanner {

    private static final String DEFAULT_FILTER = "^com.shiji.png.droid";

    private final Context context;

    private final Pattern pattern;

    AndroidServiceScanner(Context context) {
        this(context, DEFAULT_FILTER);
    }

    AndroidServiceScanner(Context context, String regex) {
        this(context, Pattern.compile(regex));
    }

    AndroidServiceScanner(Context context, Pattern pattern) {
        this.context = context;
        this.pattern = pattern;
    }

    @Override
    public Observable<Class<?>> scan() {
        return DexScanner.scan(context)
                .subscribeOn(Schedulers.io())
                .filter(s -> pattern.matcher(s).find())
                .map(Class::forName)
                .filter(c -> c.isAnnotationPresent(ServiceDef.class))
                .map(c -> c);

    }
}
