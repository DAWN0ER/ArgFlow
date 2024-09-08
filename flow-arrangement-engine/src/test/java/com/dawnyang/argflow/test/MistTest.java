package com.dawnyang.argflow.test;

import com.dawnyang.argflow.api.UidGenerator;
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

    private UidGenerator generator = MistUidGenerator.getInstance();

    @Test
    public void testDuplicate() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(10);
        int loop = 20000;
        double len = loop;
        Set<Long> set = Collections.synchronizedSet(new HashSet<>());
        StopWatch watch = new StopWatch();
        watch.start();
        while (loop-- > 0) {
            service.submit(() -> {
                long uid = generator.getUid();
                if(set.contains(uid)){
                    System.out.println(uid);
                }
                set.add(uid);
            });
        }
        service.shutdown();
        boolean awaitTermination = service.awaitTermination(20, TimeUnit.SECONDS);
        watch.stop();
        System.out.println(watch.getTime(TimeUnit.MILLISECONDS));
        System.out.println(len / watch.getTime(TimeUnit.MILLISECONDS));
        System.out.println(set.size() / len);
        long uid = generator.getUid();
        System.out.println(uid);
    }

    @Test
    public void testFunc(){
        for (int i = 0; i < 10; i++) {
            System.out.println(generator.getUid());
        }
    }

    @Test
    public void testSync() throws Exception{
        ExecutorService service = Executors.newFixedThreadPool(10);
        int loop = 100;
        double len = loop;
        StopWatch watch = new StopWatch();
        watch.start();
        while (loop-- > 0) {
            service.submit(() -> {
                long uid = generator.getUid();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
        boolean awaitTermination = service.awaitTermination(20, TimeUnit.SECONDS);
        watch.stop();
        System.out.println(watch.getTime(TimeUnit.MILLISECONDS));
    }

}
