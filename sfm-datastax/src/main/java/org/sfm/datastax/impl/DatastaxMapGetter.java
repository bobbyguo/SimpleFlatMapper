package org.sfm.datastax.impl;

import com.datastax.driver.core.GettableByIndexData;
import org.sfm.reflect.Getter;

import java.util.List;
import java.util.Map;

public class DatastaxMapGetter<K, V> implements Getter<GettableByIndexData, Map<K, V>> {

    private final int index;
    private final Class<K> keyType;
    private final Class<V> valueType;

    public DatastaxMapGetter(int index, Class<K> keyType, Class<V> valueType) {
        this.index = index;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public Map<K, V> get(GettableByIndexData target) throws Exception {
        return target.getMap(index, keyType, valueType);
    }
}