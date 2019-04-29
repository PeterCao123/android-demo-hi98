package com.shiji.png.pat.printer.usdk;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.shiji.png.pat.printer.PrinterContext;
import com.shiji.png.pat.printer.PrinterException;
import com.usdk.apiservice.aidl.UDeviceService;
import com.usdk.apiservice.aidl.printer.UPrinter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author bruce.wu
 * @since 2018/9/27
 */
public class USdkDevice {

    private static final String TAG = "USdkDevice";

    private static final String UD_ACTION_NAME = "com.usdk.apiservice";
    private static final String UD_PACKAGE_NAME = "com.usdk.apiservice";

    private static final String ARG_EMV_LOG = "emvLog";
    private static final String ARG_COMMON_LOG = "commonLog";

    public static USdkDevice create() {
        return new USdkDevice(PrinterContext.app());
    }

    private volatile UDeviceService deviceService = null;

    private final Application application;
    private final ServiceConnection sc = new ServiceConnectionImpl();
    private final CountDownLatch latch = new CountDownLatch(1);

    private USdkDevice(Application application) {
        this.application = application;
    }

    public USdkPrinter getPrinter() {
        if (!isReady()) {
            Log.w(TAG, "device service is not ready");
            throw new PrinterException("device is not connected");
        }
        try {
            return new USdkPrinter(UPrinter.Stub.asInterface(deviceService.getPrinter()));
        } catch (RemoteException e) {
            Log.e(TAG, "get device printer service failed", e);
            throw new PrinterException("connect to printer failed", e);
        }
    }

    private boolean isReady() {
        return deviceService != null;
    }

    public void connect() {
        Log.d(TAG, "connecting to device service");

        Intent intent = new Intent();
        intent.setAction(UD_ACTION_NAME);
        intent.setPackage(UD_PACKAGE_NAME);

        if (!application.bindService(intent, sc, Context.BIND_AUTO_CREATE)) {
            Log.e(TAG, "bind to device failed");
            throw new PrinterException("bind to device failed");
        }

        try {
            if (!latch.await(30, TimeUnit.SECONDS)) {
                Log.e(TAG, "connect to device timeout");
                throw new PrinterException("connect to device timeout");
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "connect to device interrupted");
            throw new PrinterException("connect to device interrupted");
        }

        if (!isReady()) {
            Log.e(TAG, "connect to device failed");
            throw new PrinterException("connect to device failed");
        }

        Log.i(TAG, "bind device service successfully");
    }

    public void disconnect() {
        if (isReady()) {
            try {
                logout();
            } catch (RemoteException e) {
                Log.e(TAG, "logout error", e);
            }
        }
        application.unbindService(sc);
    }

    private void logout() throws RemoteException {
        deviceService.unregister(null);
    }

    private class ServiceConnectionImpl implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "device service connected");
            deviceService = UDeviceService.Stub.asInterface(service);
            try {
                deviceService.register(null, new Binder());
                deviceService.debugLog(getDebugLogArgs());
                latch.countDown();
            } catch (RemoteException e) {
                //Log.e(TAG, "init device service failed", e);
                deviceService = null;
                latch.countDown();
                throw new RuntimeException("init device service failed", e);
            }

            try {
                linkToDeath(service);
            } catch (RemoteException e) {
                //Log.e(TAG, "device service link to death error", e);
                throw new RuntimeException("device service link to death error", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "device service disconnected");
            deviceService = null;
        }

        private void linkToDeath(IBinder service) throws RemoteException {
            service.linkToDeath(() -> Log.e(TAG, "device connection is dead..."), 0);
        }

        private Bundle getDebugLogArgs() {
            Bundle bundle = new Bundle();
            bundle.putBoolean(ARG_EMV_LOG, true);
            bundle.putBoolean(ARG_COMMON_LOG, true);
            return bundle;
        }
    }

}
