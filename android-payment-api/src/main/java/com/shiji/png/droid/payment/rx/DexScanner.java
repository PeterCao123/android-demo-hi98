package com.shiji.png.droid.payment.rx;

import android.content.Context;

import com.shiji.png.droid.payment.util.ClassUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author bruce.wu
 * @since 2018/11/14 16:28
 */
public final class DexScanner {

    private static final Logger logger = LoggerFactory.getLogger("RxScanner");

    public static Observable<String> scan(Context context) {
        return Observable.create((ObservableEmitter<String> emitter) -> {
            try {
                List<String> paths = ClassUtils.getSourcePaths(context);
                for (String path : paths) {
                    emitter.onNext(path);
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(new IOException("detect scan path error", e));
            }
        })
                .flatMap(path -> Observable.create(new DexObservable(path)));
    }

    private static class DexObservable implements ObservableOnSubscribe<String> {

        private final String path;

        DexObservable(String path) {
            this.path = path;
        }

        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            DexFile dexfile = null;

            logger.info("scanning {}", path);

            try {
                dexfile = ClassUtils.loadDex(path);

                Enumeration<String> dexEntries = dexfile.entries();
                while (dexEntries.hasMoreElements()) {
                    emitter.onNext(dexEntries.nextElement());
                }
            } catch (Throwable e) {
                logger.error("Scan dex file({}) error.", path, e);
            } finally {
                emitter.onComplete();
                if (null != dexfile) {
                    try {
                        dexfile.close();
                    } catch (Throwable ignore) {
                    }
                }
            }
        }
    }

}
