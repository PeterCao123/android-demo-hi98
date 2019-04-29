package com.shiji.png.pat.printer;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bruce.wu
 * @since 2019/1/30 17:20
 */
public abstract class PrinterLoader {

    private static final Map<Class<?>, Object> cache = new ConcurrentHashMap<>();

    public static PrinterService discover() {
        if (cache.containsKey(PrinterService.class)) {
            return (PrinterService) cache.get(PrinterService.class);
        }

        ServiceLoader<PrinterService> loader = ServiceLoader.load(PrinterService.class);
        Iterator<PrinterService> it = loader.iterator();
        if (!it.hasNext()) {
            throw new RuntimeException("Can not load printer service");
        }

        PrinterService instance = it.next();
        cache.put(PrinterService.class, instance);
        return instance;
    }

}
