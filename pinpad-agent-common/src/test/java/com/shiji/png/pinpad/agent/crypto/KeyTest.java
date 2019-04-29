package com.shiji.png.pinpad.agent.crypto;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author bruce.wu
 * @since 2018/12/6 9:30
 */
public class KeyTest {

    @Test
    public void string2Byte() {
        Key key = new Key();

        byte[] encryptedKEK = Hex.getDecoder().decode("83DF900A4F6FA52820540401214BA658C4EF71FFE6FC0282");
        byte[] signKEK = Hex.getDecoder().decode("38B2139CD96F09DB");
//        System.out.println(Arrays.toString(encryptedKEK));
//        System.out.println(Arrays.toString(signKEK));
        key.setKEK(encryptedKEK, signKEK);
//        System.out.println(Arrays.toString(key.getKEK()));

        byte[] encryptedDTK = Hex.getDecoder().decode("F97B4F8059F7D202E1BED5436EC8C3366CE5D440D1E22CA5");
        byte[] signDTK = Hex.getDecoder().decode("490C06D7E3AFF33C");
//        System.out.println(Arrays.toString(encryptedDTK));
//        System.out.println(Arrays.toString(signDTK));
        key.setDTK(encryptedDTK, signDTK);
//        System.out.println(Arrays.toString(key.getDTK()));
//
//        System.out.println(Sensitive.getEncoder().encode(null, key.getDTK()));
//        System.out.println(Sensitive.getEncoder().encode("", key.getDTK()));
//        System.out.println(Sensitive.getEncoder().encode("h", key.getDTK()));
//        System.out.println(Sensitive.getEncoder().encode("he", key.getDTK()));
//        System.out.println(Sensitive.getEncoder().encode("hel", key.getDTK()));
//        System.out.println(Sensitive.getEncoder().encode("hell", key.getDTK()));
//        System.out.println(Sensitive.getEncoder().encode("hello", key.getDTK()));
//        System.out.println(Sensitive.getEncoder().encode("hello world! lon", key.getDTK()));
//        System.out.println(Sensitive.getEncoder().encode("hello world! long", key.getDTK()));
//        System.out.println(Sensitive.getEncoder().encode("hello world! long long long string", key.getDTK()));
//
//        System.out.println(Sensitive.getEncoder().encode("/", key.getDTK()));
    }

    @Test
    public void byte2hex() {
        byte[] src = new byte[] {0x0, 0x7F, (byte)0x80, (byte)0xFF};
//        System.out.println(Hex.getEncoder().encode(src));
    }

    @Test
    public void dec() {
        Key key = new Key();
        byte[] encryptedKEK = Hex.getDecoder().decode("83DF900A4F6FA52820540401214BA658C4EF71FFE6FC0282");
        byte[] signKEK = Hex.getDecoder().decode("38B2139CD96F09DB");
        key.setKEK(encryptedKEK, signKEK);
        byte[] encryptedDTK = Hex.getDecoder().decode("F97B4F8059F7D202E1BED5436EC8C3366CE5D440D1E22CA5");
        byte[] signDTK = Hex.getDecoder().decode("490C06D7E3AFF33C");
        key.setDTK(encryptedDTK, signDTK);

        String plain = "6222021901001234567";
        String cipher = Sensitive.getEncoder().encode(plain, key.getDTK());
//        System.out.println(cipher);

        String plain1 = Sensitive.getDecoder().decode(cipher, key.getDTK());
//        System.out.println(plain1);

        assertEquals(plain, plain1);
    }

}