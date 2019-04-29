package com.shiji.png.pinpad.agent;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import com.shiji.png.payment.util.Json;
import com.shiji.png.pinpad.agent.crypto.Key;
import com.shiji.png.pinpad.agent.handler.CommandProcessor;
import com.shiji.png.pinpad.agent.handler.Preprocessor;
import com.shiji.png.pinpad.agent.model.TransactionDto;
import com.shiji.png.sdk.java.link.clientSDK.ws.CallbackMessageProcessor;
import com.shiji.png.sdk.java.link.clientSDK.ws.MessageHandler;
import com.shiji.png.sdk.java.link.clientSDK.ws.WsSessionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class MyService extends Service {

    private static final Logger logger = LoggerFactory.getLogger("MyService");

    public static void start(Context context) {
        if (MyService.getState() == STATE_STOPPED) {
            Intent intent = new Intent(context, MyService.class);
            context.startService(intent);
        }
    }

    public static void stop(Context context) {
        if (MyService.getState() == STATE_RUNNING) {
            Intent intent = new Intent(context, MyService.class);
            context.stopService(intent);
        }
    }

    public static final int STATE_STOPPED = 0;
    public static final int STATE_RUNNING = 2;

    private static final AtomicInteger state = new AtomicInteger(STATE_STOPPED);

    private static volatile long startTime = 0;

    private AgentConfig config;

    private WsSessionManager wsSessionManager;

    public static int getState() {
        return state.get();
    }

    public static long getRunningTime() {
        if (startTime == 0) {
            return 0;
        }
        return System.currentTimeMillis() - startTime;
    }

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.info("onCreate...");

        config = new AgentConfig(getApplicationContext());
        if (config.validate()) {
            startServer();
        } else {
            logger.error("Invalid config");
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        stopServer();
        super.onDestroy();
        logger.info("onDestroy...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private void startWebSocketSession(final CommandProcessor processor) {
        MessageHandler messageHandler = s -> {
            logger.trace("handler recv: {}", s);
            TransactionDto msg = Json.getDecoder().decode(s, TransactionDto.class);
            TransactionDto dto = processor.process(msg);
            String out = Json.getEncoder().encode(dto);
            logger.trace("handler sent: {}", out);
            return out;
        };
        CallbackMessageProcessor messageProcessor = new CallbackMessageProcessor(messageHandler);
        wsSessionManager = new WsSessionManager(config.getWsConfig(), messageProcessor);
        wsSessionManager.startRunning();
    }

    private void stopWebSocketSession() {
        if (wsSessionManager != null) {
            wsSessionManager.shutdown();
            wsSessionManager = null;
        }
    }

    private void startServer() {
        if (!state.compareAndSet(STATE_STOPPED, STATE_RUNNING)) {
            logger.debug("service is already running, state={}", state.get());
            return;
        }
        final CommandProcessor processor = getCommandProcessor(null);
        startWebSocketSession(processor);
        startTime = System.currentTimeMillis();
    }

    private void stopServer() {
        if (!state.compareAndSet(STATE_RUNNING, STATE_STOPPED)) {
            logger.debug("service is already stopped, state={}", state.get());
            return;
        }
        startTime = 0;
        stopWebSocketSession();
    }

    private CommandProcessor getCommandProcessor(Key key) {
        final Preprocessor preprocessor = new Preprocessor() {
            @Override
            public void process(TransactionDto dto) {
                MyService.this.wakeUp();

                dto.setDeviceBrand(Build.MANUFACTURER);
                dto.setDeviceModel(Build.PRODUCT);
            }
        };
        return new CommandProcessor(key, preprocessor);
    }

    private void wakeUp() {
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            int flag = PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK;
            PowerManager.WakeLock wl = pm.newWakeLock(flag, "ppa:bright");
            wl.acquire(500);
            wl.release();
        }

        //unlock
        KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("ppa:unlock");
        //kl.reenableKeyguard();
        kl.disableKeyguard();

        //play sound
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(this, notification);
        r.play();
    }

}
