package com.auth.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hash(String input) {
        return BCrypt.hashpw(input, BCrypt.gensalt());
    }

    public static boolean verify(String input, String hash) {
        return BCrypt.checkpw(input, hash);
    }
}
