package com.juvenxu.mvnbook.account.captcha;

import java.util.Random;

/**
 * Created by lh on 17-3-13.
 */
public class RandomGenerator {
    private static String range = "0123456789abcdefghijklmonpqrstuvwxyz";

    public static String getRandomString() {
        Random random = new Random();

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            result.append(range.charAt(random.nextInt(range.length())));
        }

        return result.toString();
    }
}
