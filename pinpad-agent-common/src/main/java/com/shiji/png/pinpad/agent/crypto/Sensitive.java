package com.shiji.png.pinpad.agent.crypto;

/**
 * @author bruce.wu
 * @since 2018/11/28 11:23
 */
public class Sensitive {

    public static Encoder getEncoder() {
        return Encoder.INSTANCE;
    }

    public static Decoder getDecoder() {
        return Decoder.INSTANCE;
    }

    public static class Encoder {

        static final Encoder INSTANCE = new Encoder();

        public String encode(String plain, byte[] key) {
            if (plain == null || plain.isEmpty()) {
                return "";
            }
            String padding;
            String tail = "";
            if (plain.length() >= 16) {
                int paddingEnd = plain.length() % 16;
                padding = plain.substring(0, plain.length() - paddingEnd);
                tail = plain.substring(padding.length());
            } else {
                padding = plain;
                int paddingEnd = 16 - plain.length();
                for (int i = 0; i < paddingEnd; i++) {
                    padding = padding.concat("F");
                }
            }
            byte[] src = Hex.getDecoder().decode(padding);
            byte[] data = TripleDes.getEncoder().encode(src, key);
            return Hex.getEncoder().encode(data) + tail;
        }

    }

    public static class Decoder {

        static final Decoder INSTANCE = new Decoder();

        public String decode(String text, byte[] key) {
            if (text == null || text.isEmpty()) {
                return "";
            }

            int tail = 0;
            String src;
            if (text.length() > 16) {
                tail = text.length() - text.length() % 16;
                src = text.substring(0, tail);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(text);
                for (int i = text.length(); i < 16; i++) {
                    sb.append('0');
                }
                src = sb.toString();
            }

            byte[] data = Hex.getDecoder().decode(src);
            byte[] plain = TripleDes.getDecoder().decode(data, key);

            String plainText = Hex.getEncoder().encode(plain);
            if (tail > 0) {
                plainText += text.substring(tail);
            } else {
                int idx = plainText.indexOf("F");
                if (idx >= 0) {
                    plainText = plainText.substring(0, idx);
                }
            }
            return plainText;
        }

    }

}
