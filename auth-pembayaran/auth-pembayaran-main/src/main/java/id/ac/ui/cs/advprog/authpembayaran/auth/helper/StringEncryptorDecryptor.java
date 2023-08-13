package id.ac.ui.cs.advprog.authpembayaran.auth.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class StringEncryptorDecryptor {
    static final String SECRETS = "secretskey";

    private static SecretKeySpec secretKey;

    private static final String ALGORITHM = "AES";

    private StringEncryptorDecryptor() {
        throw new IllegalStateException("Utility Class");
    }

    public static void prepareSecretKey(String myKey) {
        byte[] key;
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt) {
        try {
            prepareSecretKey(SECRETS);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)))
                    .replace("/", "-");
        } catch (Exception e) {
            return null;
        }

    }

    public static String decrypt(String strToDecrypt) {
        try {
            prepareSecretKey(SECRETS);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt.replace("-", "/"))));
        } catch (Exception e) {
            return null;
        }

    }

}