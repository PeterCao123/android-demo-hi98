package com.shiji.png.pat.app.router;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shiji.png.droid.payment.rx.DexScanner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

/**
 * @author bruce.wu
 * @since 2019/2/11 11:37
 */
public final class ARouter {

    private static final String TAG = "ARouter";

    private volatile static Application application;

    //route table
    private static final Map<String, Class<?>> table = new HashMap<>();

    public static void init(Application appContext) {
        init(appContext, "com.shiji.png");
    }

    public static void init(Application appContext, String prefix) {
        application = appContext;
        DexScanner.scan(appContext)
                .subscribeOn(Schedulers.io())
                .filter(s -> s.startsWith(prefix))
                .map(Class::forName)
                .filter(c -> c.isAnnotationPresent(Route.class))
                .doOnNext(c -> {
                    Route annotation = c.getAnnotation(Route.class);
                    table.put(annotation.path(), c);
                })
                .blockingSubscribe();
        Log.i(TAG, "route table size: " + table.size());
        for (Map.Entry<String, Class<?>> entry : table.entrySet()) {
            Log.d(TAG, "load route: " + entry.getKey() + " => " + entry.getValue().getSimpleName());
        }
    }

    public static ARouter getInstance() {
        if (application == null) {
            throw new NullPointerException("application is null");
        }
        synchronized (ARouter.class) {
            return new ARouter();
        }
    }

    private String path;
    private final Bundle bundle = new Bundle();

    private ARouter() {

    }

    public ARouter build(String path) {
        if (!table.containsKey(path)) {
            throw new NullPointerException("no route: " + path);
        }
        this.path = path;
        return this;
    }

    public ARouter withDouble(String key, double value) {
        bundle.putDouble(key, value);
        return this;
    }

    public ARouter withBoolean(String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public ARouter withString(String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public void navigation() {
        Class<?> activity = table.get(path);
        Intent intent = new Intent(application, activity);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    public void inject(Object obj) {
        if (obj instanceof Activity) {
            inject((Activity)obj);
            return;
        }
        throw new RuntimeException("Can not inject type: " + obj.getClass().getName());
    }

    private void inject(Activity activity) {
        Intent intent = activity.getIntent();
        Field[] fields = activity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            Autowired annotation = field.getAnnotation(Autowired.class);
            String name = annotation.name();
            if (!intent.hasExtra(name)) {
                continue;
            }
            try {
                field.setAccessible(true);
                if (boolean.class.equals(field.getType())) {
                    boolean value = intent.getBooleanExtra(name, false);
                    field.setBoolean(activity, value);
                } else if (String.class.equals(field.getType())) {
                    String value = intent.getStringExtra(name);
                    field.set(activity, value);
                } else if (double.class.equals(field.getType())) {
                    double value = intent.getDoubleExtra(name, 0D);
                    field.setDouble(activity, value);
                }
            } catch (Exception e) {
                Log.e(TAG, "inject field: " + field.getName(), e);
            }
        }
    }

}
