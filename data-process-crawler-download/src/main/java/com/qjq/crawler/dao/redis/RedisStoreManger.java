package com.qjq.crawler.dao.redis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.activemq.util.ByteArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisStoreManger {

    @Autowired
    JedisPool jedisPool;

    public void putToRedis(String key, String field, Object value, Integer milliseconds) throws Exception {
        Assert.hasText(key);
        Assert.notNull(value);
        Assert.isTrue(value instanceof Serializable);
        Assert.isTrue(milliseconds == null || milliseconds > 0);

        byte[] binaryValue;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            binaryValue = serialObject(value);
            if (field == null)
                jedis.set(key.getBytes(), binaryValue);
            else
                jedis.hset(key.getBytes(), field.getBytes(), binaryValue);
            if (milliseconds != null)
                jedis.pexpire(key, milliseconds);
        } catch (IOException e) {
            throw new Exception("序列化对象失败", e);
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * 
     * 根据key 获取对象 <br/>
     * 
     * @param key
     * @return 返回key 对应的值，如果不存在将返回空
     * @throws Exception
     */
    public <T> T getByRedis(String key, String field, Class<T> type) throws Exception {
        Assert.hasText(key);
        T result = null;
        byte[] data;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (field != null) {
                data = jedis.hget(key.getBytes(), field.getBytes());
            } else {
                data = jedis.get(key.getBytes());
            }
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        if (data != null) {
            result = (T) deseriaObject(data);
        }
        return result;
    }

    public <T> List<T> getArrayByRedis(String[] keys, Class<T> type) throws Exception {
        Assert.notEmpty(keys);
        List<T> results = new ArrayList<>(keys.length);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[][] binaryKeys = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++) {
                binaryKeys[i] = keys[i].getBytes();
            }
            List<byte[]> values = jedis.mget(binaryKeys);
            for (byte[] v : values) {
                if (v != null) {
                    results.add((T) deseriaObject(v));
                }
            }
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return results;
    }

    public Set<String> getHkeysByRedis(String keys) throws Exception {
        Assert.hasText(keys);
        Set<String> results = new HashSet<>();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            results = jedis.hkeys(keys);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return results;
    }

    public void putToRedisByHmset(String key, Map<String, ? extends Serializable> map) throws Exception {
        Assert.hasText(key);
        Assert.notEmpty(map);
        Map<byte[], byte[]> hash = new HashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if (!StringUtils.isEmpty(entry.getKey()) && null != entry.getValue()) {
                try {
                    byte[] binaryValue = serialObject(entry.getValue());
                    hash.put(entry.getKey().getBytes(), binaryValue);
                } catch (IOException e) {
                    // throw new Exception("序列化对象失败", e);
                }
            }
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hmset(key.getBytes(), hash);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public boolean existByRedis(String key, String field) throws Exception {
        Assert.hasText(key);
        Boolean result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (field != null)
                result = jedis.hexists(key, field);
            else
                result = jedis.exists(key);
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 
     * 移除指定key <br/>
     * 
     * @param key
     */
    public void removeByRedis(String key, String field) throws Exception {
        Assert.hasText(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (field != null)
                jedis.hdel(key, field);
            else
                jedis.del(key);
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    private static byte[] serialObject(Object obj) throws IOException {
        byte[] result = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(output);
        out.writeObject(obj);
        out.close();
        result = compress(output.toByteArray());
        return result;
    }

    private static Object deseriaObject(byte[] binaryObj) throws Exception {
        Object result = null;
        byte[] data = decompress(binaryObj);
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(data));
        input.close();
        result = input.readObject();
        return result;
    }

    /**
     * 压缩
     * 
     * @param data 待压缩数据
     * @return byte[] 压缩后的数据
     */
    private static byte[] compress(byte[] data) {
        byte[] output = new byte[0];
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        compresser.end();
        return output;
    }

    /**
     * 解压缩
     * 
     * @param data 待压缩的数据
     * @return byte[] 解压缩后的数据
     * @throws DataFormatException
     * @throws IOException
     */
    private static byte[] decompress(byte[] data) throws DataFormatException, IOException {
        byte[] output = new byte[0];
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[1024];
        while (!decompresser.finished()) {
            int i = decompresser.inflate(buf);
            o.write(buf, 0, i);
        }
        output = o.toByteArray();
        o.close();
        decompresser.end();
        return output;
    }

    /**
     * 计数器减1
     * 
     * @param key
     * @return
     */
    public long decr(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            long value = jedis.decr(key);
            return value;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 计数器加1
     * 
     * @param key
     * @return
     */
    public long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            long value = jedis.incr(key);
            return value;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public void hset(String key, int value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(key, seconds, String.valueOf(value));
        } catch (Exception e) {
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            String str = jedis.get(key);
            return str;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

}
