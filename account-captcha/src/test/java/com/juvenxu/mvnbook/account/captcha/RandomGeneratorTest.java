package com.juvenxu.mvnbook.account.captcha;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertFalse;

/**
 * Created by lh on 17-3-15.
 */
public class RandomGeneratorTest {

    @Test
    public void testGetRandomString() {
        Set<String> randoms = new HashSet<>(100);

        for (int i = 0; i < 100; i++) {
            String random = RandomGenerator.getRandomString();

            assertFalse(randoms.contains(random));
            System.out.println("random :"+random);
            System.out.println("randoms" + randoms + "contains: " + randoms.contains(random));
            randoms.add(random);
        }
    }

}
