package com.shiji.png.payment;

import com.shiji.png.payment.annotation.ServiceDef;
import com.shiji.png.payment.annotation.ServiceResource;
import com.shiji.png.payment.annotation.ServiceType;
import com.shiji.png.payment.rx.EmptyObserver;
import com.shiji.png.payment.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author bruce.wu
 * @since 2018/11/15 11:14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServiceManager {

    private static final Logger logger = LoggerFactory.getLogger("ServiceManager");

    private static final Map<String, ServiceInfo> registry;

    static {
        registry = new ConcurrentHashMap<>();
    }

    private static ServiceScanner scanner;
    private static ServiceSelector selector;

    public static void assignScanner(ServiceScanner scanner) {
        ServiceManager.scanner = scanner;
    }

    public static void assignSelector(ServiceSelector selector) {
        ServiceManager.selector = selector;
    }

    private static synchronized void loadIfNeeded() {
        if (!registry.isEmpty()) {
            return;
        }

        if (scanner == null) {
            throw new NullPointerException("service scanner is null");
        }

        scanner.scan()
                .doOnNext(ServiceManager::register)
                .doOnError(tr -> logger.error("load service error", tr))
                .blockingSubscribe(new EmptyObserver<>());

        if (registry.isEmpty()) {
            throw new LoadServiceException("service not found");
        }
    }

    public static void register(Class<?> type) {
        if (!type.isAnnotationPresent(ServiceDef.class)) {
            throw new LoadServiceException("unknown: " + type.getName());
        }
        register(type.getAnnotation(ServiceDef.class));
    }

    private static void register(ServiceDef annotation) {
        logger.info("found service name={}, type={}, factory={}",
                annotation.name(), annotation.type(), annotation.factory().getName());
        if (registry.containsKey(annotation.name())) {
            throw new LoadServiceException("duplicated: " + annotation.name());
        }
        registry.put(annotation.name(), buildServiceInfo(annotation));
    }

    private static ServiceInfo buildServiceInfo(ServiceDef annotation) {
        String displayText = null;
        try {
            ServiceResource resource = newInstance(annotation.resource());
            displayText = resource.getDisplayText();
        } catch (Exception ignore) {

        }
        if (StringUtils.isEmpty(displayText)) {
            displayText = annotation.name();
        }
        return new ServiceInfo(annotation.name(), displayText, annotation.type(), annotation.factory());
    }

    public static Observable<ServiceInfo> services() {
        try {
            loadIfNeeded();
            return Observable.fromIterable(registry.values());
        } catch (LoadServiceException e) {
            return Observable.error(e);
        }
    }

    public static Observable<ServiceInfo> services(ServiceType serviceType) {
        return services().filter(s -> s.getType() == serviceType);
    }

    public static Observable<ServiceInfo> select() {
        return selector.select();
    }

    public static Observable<ServiceInfo> select(String amount) {
        return selector.select(amount);
    }

    public static List<ServiceInfo> getServiceList() {
        return services().toList().blockingGet();
    }

    public static List<ServiceInfo> getServiceList(ServiceType serviceType) {
        return services(serviceType).toList().blockingGet();
    }

    /**
     * select a payment service manually
     */
    public static ServiceInfo selectOne() {
        return select().blockingFirst();
    }

    public static boolean exists(ServiceType serviceType) {
        return services(serviceType).count().blockingGet() > 0;
    }

    public static PaymentService create(String serviceName) {
        loadIfNeeded();
        if (!registry.containsKey(serviceName)) {
            logger.error("no such service: {}", serviceName);
            throw new NullPointerException(serviceName);
        }
        return create(registry.get(serviceName));
    }

    public static PaymentService create(ServiceInfo serviceInfo) {
        try {
            ServiceFactory factory = newInstance(serviceInfo.getFactory());
            return factory.create();
        } catch (Exception e) {
            logger.error("create service[{}] by factory[{}] failed",
                    serviceInfo, serviceInfo.getFactory().getName(), e);
            throw new RuntimeException(serviceInfo.getName(), e);
        }
    }

    private static <T> T newInstance(Class<T> type) throws Exception {
        Constructor<T> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

}
