package com.dawnyang.argflow.utils;

import com.dawnyang.argflow.api.UidGenerator;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: 迷雾算法 taskUid 生成器
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/19:16
 */
public class MistUidGenerator implements UidGenerator {

    private final long START = 1704038400000L; //2024-01-01 00:00:00
    private final AtomicLong inc = new AtomicLong(System.currentTimeMillis() - START);
    private volatile static UidGenerator instance;

    private MistUidGenerator() {}

    public static UidGenerator getInstance() {
        // 懒加载单例
        if (Objects.isNull(instance)) {
            synchronized (MistUidGenerator.class) {
                if (Objects.isNull(instance)){
                    instance = new MistUidGenerator();
                    return instance;
                }
            }
        }
        return instance;
    }

    @Override
    public long getUid() {
        int r1 = ThreadLocalRandom.current().nextInt(256);
        int r2 = ThreadLocalRandom.current().nextInt(256);
        long current = inc.incrementAndGet();
        long currentTime = System.currentTimeMillis() - START;
        if (current < currentTime) {
            synchronized (MistUidGenerator.class) {
                if (inc.get() < currentTime) {
                    inc.set(currentTime);
                }
            }
        }
        return current << 16 | r2 << 8 | r1;
    }

}
