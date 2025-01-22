package ject.componote.domain.auth.util;

import ject.componote.domain.auth.error.InvalidEncryptionSecretKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.function.Function;

@Component
public class AESCryptography {
    public static final String AES_ALGORITHM = "AES";
    public static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding"; // 블록 암호화 모드와 패딩
    public static final int AES_SECRET_KEY_LENGTH = 16;

    private final SecretKeySpec keySpec;

    public AESCryptography(@Value("${encryption.aes.key}") String secretKey) {
        validateSecretKey(secretKey);
        this.keySpec = new SecretKeySpec(secretKey.getBytes(), AES_ALGORITHM);
    }

    public <T> String encrypt(final T rawData) {
        return encrypt(String.valueOf(rawData));
    }

    public <T extends Number> T decrypt(final String encryptedData, final Function<String, T> parser) {
        final String decryptedData = decrypt(encryptedData);
        try {
            return parser.apply(decryptedData);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String encrypt(final String rawData) {
        validateData(rawData);
        try {
            final Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            final byte[] encrypted = cipher.doFinal(rawData.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String decrypt(final String encryptedData) {
        try {
            final Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            final byte[] decoded = Base64.getDecoder().decode(encryptedData);
            final byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void validateSecretKey(final String secretKey) {
        if (secretKey == null || secretKey.length() != AES_SECRET_KEY_LENGTH) {
            throw new InvalidEncryptionSecretKeyException(secretKey);
        }
    }

    private void validateData(final String data) {
        if (data == null || data.isBlank()) {
            throw new IllegalArgumentException("data is null or empty");
        }
    }
}
