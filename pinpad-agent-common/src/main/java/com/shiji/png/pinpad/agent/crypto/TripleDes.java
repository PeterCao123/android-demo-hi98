package com.shiji.png.pinpad.agent.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * @author bruce.wu
 * @since 2018/11/27 17:24
 */
public class TripleDes {

    public static Encoder getEncoder() {
        return Encoder.INSTANCE;
    }

    public static Decoder getDecoder() {
        return Decoder.INSTANCE;
    }

    public static class Decoder {

        static final Decoder INSTANCE = new Decoder();

        public byte[] decode(byte[] src, byte[] key) {
            try {
                DESedeKeySpec keySpec = new DESedeKeySpec(key);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
                SecretKey secretKey = keyFactory.generateSecret(keySpec);
                Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return cipher.doFinal(src);
            } catch (Exception e) {
                throw new RuntimeException("decode failed", e);
            }
        }

    }

    public static class Encoder {

        static final Encoder INSTANCE = new Encoder();

        public byte[] encode(byte[] src, byte[] key) {
            try {
                DESedeKeySpec keySpec = new DESedeKeySpec(key);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
                SecretKey secretKey = keyFactory.generateSecret(keySpec);
                Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return cipher.doFinal(src);
            } catch (Exception e) {
                throw new RuntimeException("encode failed", e);
            }
        }

    }

}
