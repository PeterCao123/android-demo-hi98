package com.shiji.png.payment.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author bruce.wu
 * @since 2018/11/15 10:43
 */
public class Json {

    public static Encoder getEncoder() {
        return Encoder.INSTANCE;
    }

    public static Decoder getDecoder() {
        return Decoder.INSTANCE;
    }

    @Override
    public String toString() {
        return getEncoder().encode(this);
    }

    public static class Encoder {

        private Encoder() {}

        static final Encoder INSTANCE = new Encoder();

        public String encode(Object value) {
            return Impl.GSON.toJson(value);
        }

    }

    public static class Decoder {

        private Decoder() {}

        static final Decoder INSTANCE = new Decoder();

        public <T> T decode(String json, Class<T> type) {
            return Impl.GSON.fromJson(json, type);
        }

    }

    public static class Impl {

        static final Gson GSON;

        static {
            GSON = new GsonBuilder().create();
        }
    }

}
