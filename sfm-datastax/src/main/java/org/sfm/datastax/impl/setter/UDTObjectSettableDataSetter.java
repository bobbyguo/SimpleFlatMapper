package org.sfm.datastax.impl.setter;

import com.datastax.driver.core.*;
import com.datastax.driver.core.schemabuilder.UDTType;
import org.sfm.datastax.DatastaxColumnKey;
import org.sfm.datastax.SettableDataMapperBuilder;
import org.sfm.datastax.impl.SettableDataSetterFactory;
import org.sfm.map.Mapper;
import org.sfm.map.MapperConfig;
import org.sfm.map.column.FieldMapperColumnDefinition;
import org.sfm.map.mapper.ConstantTargetFieldMapperFactorImpl;
import org.sfm.reflect.ReflectionService;
import org.sfm.reflect.Setter;
import org.sfm.reflect.meta.ClassMeta;
import org.sfm.tuples.Tuple2;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

public class UDTObjectSettableDataSetter<T> implements Setter<SettableByIndexData, T> {
    private final int index;
    private final UserType udtType;
    private final Mapper<T, SettableByIndexData> mapper;

    public UDTObjectSettableDataSetter(int index, UserType udtType, Mapper<T, SettableByIndexData> mapper) {
        this.index = index;
        this.udtType = udtType;
        this.mapper = mapper;
    }

    @Override
    public void set(SettableByIndexData target, T value) throws Exception {
        if (value == null) {
            target.setToNull(index);
        } else {
            UDTValue udtValue = udtType.newValue();
            mapper.mapTo(value, udtValue, null);
            target.setUDTValue(index, udtValue);
        }
    }

    public static <T extends Tuple2<?, ?>> Setter<SettableByIndexData, T> newInstance(Type target,  UserType tt, int index, MapperConfig<DatastaxColumnKey, FieldMapperColumnDefinition<DatastaxColumnKey>> config,
                                                                                      ReflectionService reflectionService) {
        Mapper<T, SettableByIndexData> mapper = newTupleMapper(target, tt, config, reflectionService);
        return new UDTObjectSettableDataSetter<T>(index, tt, mapper);
    }

    public static <T extends Tuple2<?, ?>> Mapper<T, SettableByIndexData> newTupleMapper(Type target, UserType tt,
                                                                                         MapperConfig<DatastaxColumnKey, FieldMapperColumnDefinition<DatastaxColumnKey>> config,
                                                                                         ReflectionService reflectionService) {
        SettableDataMapperBuilder<T> builder = newFieldMapperBuilder(config, reflectionService, target);
        Iterator<UserType.Field> iterator = tt.iterator();
        int i = 0;
        while(iterator.hasNext()) {
            UserType.Field f = iterator.next();
            builder.addColumn(new DatastaxColumnKey(f.getName(), i, f.getType()));
            i++;
        }
        return builder.mapper();
    }

    public static <T> SettableDataMapperBuilder<T> newFieldMapperBuilder(MapperConfig<DatastaxColumnKey, FieldMapperColumnDefinition<DatastaxColumnKey>> config,
                                                                         ReflectionService reflectionService,  Type target) {
        ClassMeta<T> classMeta = reflectionService.getClassMeta(target);
        return new SettableDataMapperBuilder<T>(classMeta, config, ConstantTargetFieldMapperFactorImpl.instance(new SettableDataSetterFactory(config, reflectionService)));
    }
}