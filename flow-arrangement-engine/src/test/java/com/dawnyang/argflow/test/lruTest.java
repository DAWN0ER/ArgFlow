package com.dawnyang.argflow.test;

import com.dawnyang.argflow.action.LRUCache;
import com.dawnyang.argflow.domain.task.TaskInfoDto;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/21:09
 */
public class lruTest {

    private ExecutorService service = Executors.newFixedThreadPool(10);

    @Test
    public void test() throws Exception{
        LRUCache cache = new LRUCache(5);
        for (int i = 0; i < 200; i++) {
            long id = i;
            service.submit(()->{
                TaskInfoDto dto = new TaskInfoDto();
                dto.setTaskId(id);
                cache.record(dto);
            });
        }
        service.shutdown();
        service.awaitTermination(20, TimeUnit.SECONDS);
        System.out.println(Arrays.toString(cache.keySet().toArray()));

    }
}
