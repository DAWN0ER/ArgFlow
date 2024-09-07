package com.dawnyang.argflow.utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/19:16
 */
public class MistUidGenerator {

    private static final long START = 1704038400000L; //2024-01-01 00:00:00
    private static final AtomicLong inc = new AtomicLong(System.currentTimeMillis() - START);

    public static long getUid(){
        int r1 = ThreadLocalRandom.current().nextInt(256);
        int r2 = ThreadLocalRandom.current().nextInt(256);
        long current = inc.incrementAndGet();
        long currentTime = System.currentTimeMillis() - START;
        if (current < currentTime){
            synchronized (MistUidGenerator.class){
                if (inc.get() < currentTime){
                    inc.set(currentTime);
                }
            }
        }
        return current << 16 | r2 << 8 | r1;
    }

}
