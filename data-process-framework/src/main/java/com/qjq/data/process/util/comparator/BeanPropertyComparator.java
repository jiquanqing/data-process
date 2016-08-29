/** Copyright 2013-2023 步步高商城. */
package com.qjq.data.process.util.comparator;

import java.util.Comparator;

import com.qjq.data.process.util.UtilObj;
import static com.qjq.data.process.util.UtilObj.getPropValue;

/**
 * @since 0.1.0
 */
public class BeanPropertyComparator<T> implements Comparator<T> {
    private final String name;
    private final boolean asc;

    public BeanPropertyComparator(String name) {
        this(name, true);
    }

    public BeanPropertyComparator(String name, boolean asc) {
        this.name = name;
        this.asc = asc;
    }

    @Override
    public int compare(T obj1, T obj2) {
        int ret = UtilObj.compare(getPropValue(obj1, name), getPropValue(obj2, name));
        return asc ? ret : -ret;
    }
}
