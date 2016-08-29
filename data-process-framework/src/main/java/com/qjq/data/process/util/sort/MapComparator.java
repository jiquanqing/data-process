package com.qjq.data.process.util.sort;

import java.util.Comparator;
import java.util.Map;

import com.qjq.data.process.util.UtilObj;

/**
 * 
 * @since 0.1.0
 */
public class MapComparator<K, V> implements Comparator<Map<K, V>> {
    private final K key;
    private final boolean asc;

    public MapComparator(K key) {
        this(key, true);
    }

    public MapComparator(K key, boolean asc) {
        this.key = key;
        this.asc = asc;
    }

    @Override
    public int compare(Map<K, V> map1, Map<K, V> map2) {
        int ret = UtilObj.compare(map1.get(key), map2.get(key));
        return asc ? ret : -ret;
    }

}
