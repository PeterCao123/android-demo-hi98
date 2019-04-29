package com.shiji.png.pinpad.agent.crypto;

import java.util.Arrays;

/**
 * @author bruce.wu
 * @since 2018/11/27 16:21
 */
public class Key {

    private static final byte[] MASTER_KEY = new byte[]{
            17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17
    };
    private static final byte[] CHECK_DATA = new byte[]{
            0, 0, 0, 0, 0, 0, 0, 0
    };
    private static final byte[] DEFAULT_DEK = new byte[]{
            -53, 50, -95, -14, 105, 119, -39, 28, 92, 108, -118, 87, -61, 37, 11, -127, 16, 22, 57, 112, 100, -74, 4, 108
    };
    private static final int ENCRYPTED_KEK_SIZE = 24;

    private byte[] abKEK;

    private byte[] abDTK = DEFAULT_DEK;

    public Key setKEK(String encryptedKEK, String signKEK) {
        return setKEK(encryptedKEK == null ? null : Hex.getDecoder().decode(encryptedKEK),
                signKEK == null ? null : Hex.getDecoder().decode(signKEK));
    }

    public Key setKEK(byte[] encryptedKEK, byte[] signKEK) {
        check(encryptedKEK, signKEK);
        byte[] abKEK = TripleDes.getDecoder().decode(encryptedKEK, MASTER_KEY);
        byte[] abSign = TripleDes.getEncoder().encode(CHECK_DATA, abKEK);
        if (!Arrays.equals(signKEK, abSign)) {
            throw new RuntimeException("invalid KEK signature");
        }
        byte[] abKEKCipher = new byte[ENCRYPTED_KEK_SIZE];
        System.arraycopy(encryptedKEK, 0, abKEKCipher, 0, ENCRYPTED_KEK_SIZE);
        this.abKEK = TripleDes.getDecoder().decode(abKEKCipher, MASTER_KEY);
        return this;
    }

    public Key setDTK(String encryptedDEK, String signDEK) {
        return setDTK(encryptedDEK == null ? null : Hex.getDecoder().decode(encryptedDEK),
                signDEK == null ? null : Hex.getDecoder().decode(signDEK));
    }

    public Key setDTK(byte[] encryptedDEK, byte[] signDEK) {
        check(encryptedDEK, signDEK);
        byte[] abDTK = TripleDes.getDecoder().decode(encryptedDEK, getKEK());
        byte[] abSign = TripleDes.getEncoder().encode(CHECK_DATA, abDTK);
        if (!Arrays.equals(signDEK, abSign)) {
            throw new RuntimeException("invalid DEK signature");
        }
        byte[] abDTKCipher = new byte[ENCRYPTED_KEK_SIZE];
        System.arraycopy(encryptedDEK, 0, abDTKCipher, 0, ENCRYPTED_KEK_SIZE);
        this.abDTK = TripleDes.getDecoder().decode(abDTKCipher, getKEK());
        return this;
    }

    public byte[] getKEK() {
        return this.abKEK;
    }

    public byte[] getDTK() {
        return abDTK;
    }

    private void check(byte[] encryptedKey, byte[] sign) {
        if (encryptedKey == null) {
            throw new NullPointerException("encrypted key is null");
        }
        if (encryptedKey.length < ENCRYPTED_KEK_SIZE) {
            throw new RuntimeException("encrypted key is too shorter");
        }
        if (sign == null) {
            throw new NullPointerException("key signature is null");
        }
    }

}
