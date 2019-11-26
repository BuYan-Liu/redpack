package com.zhenglong.cache;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Lion
 * @描述
 * @date 2019/9/5
 */
public class RedisCache implements Cache {

    Logger logger=LoggerFactory.getLogger(RedisCache.class);
    private final String id;
    ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();

    public RedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug("MybatisRedisCache:id=" + id);
        this.id = id;
    }

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object o, Object o1) {
        try {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set(o,o1);
            logger.debug("put query result to redis");
        }catch (Throwable e) {
            logger.error("put query result to redis failed" + e);
        }
    }

    @Override
    public Object getObject(Object o) {
        try {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            logger.debug("Get cached query from redis");
            Object val = valueOperations.get(o);
            return val;
        }catch (Throwable e) {
            logger.error("Get cached query from redis failed" + e);
        }
        return null;
    }

    @Override
    public Object removeObject(Object o) {
        try {
            redisTemplate.delete(o);
            logger.debug("delete key" + o.toString());
        }catch (Throwable e) {
            logger.error("redis remove failed" + e);
        }
        return null;
    }

    @Override
    public void clear() {
        readWriteLock.readLock().lock();

        try {
            redisTemplate.execute((RedisCallback) (connect) -> {
                connect.flushDb();
                return null;
            });
            logger.debug("clear db...");
        }finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
