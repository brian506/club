package fun.club.secure;

import java.util.Base64;

public class Validator {
    public static void main(String[] args) {
        String secretKey = "";
        boolean isValidBase64 = isValidBase64(secretKey);
        System.out.println("비밀 키가 유효한 Base64 인코딩인가요? " + isValidBase64);
    }

    public static boolean isValidBase64(String string) {
        try {
            Base64.getDecoder().decode(string);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

