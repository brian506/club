package fun.club.secure;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        // 8바이트 랜덤 바이트 생성 (64비트)
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64]; // 8바이트 (64비트)
        random.nextBytes(bytes);

        // Base64로 인코딩
        String secretKey = Base64.getUrlEncoder().encodeToString(bytes);
        System.out.println("Generated Secret Key (Base64): " + secretKey);
    }
}
