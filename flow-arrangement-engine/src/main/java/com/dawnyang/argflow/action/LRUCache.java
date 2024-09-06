package com.dawnyang.argflow.action;

import com.dawnyang.argflow.domain.task.TaskInfoDto;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/20:45
 */
public class LRUCache extends LinkedHashMap<Long, TaskInfoDto> {

    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, TaskInfoDto> eldest) {
        return size() > capacity;
    }

    public synchronized void record(TaskInfoDto taskInfoDto){
        put(taskInfoDto.getTaskId(),taskInfoDto);
    }

    public synchronized TaskInfoDto getTask(long taskId){
        return get(taskId);
    }

}
