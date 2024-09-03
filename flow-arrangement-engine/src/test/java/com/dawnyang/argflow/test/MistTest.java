package com.dawnyang.argflow.test;

import com.dawnyang.argflow.utils.MistUidGenerator;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/19:36
 */

public class MistTest {

    @Test
    public void test() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(10);
        int loop = 20000;
        double len = loop;
        Set<Long> set = Collections.synchronizedSet(new HashSet<>());
        StopWatch watch = new StopWatch();
        watch.start();
        while (loop-- > 0) {
            service.submit(() -> {
                long uid = MistUidGenerator.getUid();
                if(set.contains(uid)){
                    System.out.println(uid);
                }
                set.add(uid);
            });
        }
        service.shutdown();
        service.awaitTermination(20, TimeUnit.SECONDS);
        watch.stop();
        System.out.println(len / watch.getNanoTime() * 1e9);
        System.out.println(set.size() / len);
    }

    @Test
    public void t1(){
        for (int i = 0; i < 10; i++) {
            System.out.println(MistUidGenerator.getUid());
        }
    }

}
