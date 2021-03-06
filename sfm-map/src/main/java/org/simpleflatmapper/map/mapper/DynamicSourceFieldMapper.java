package org.simpleflatmapper.map.mapper;

import org.simpleflatmapper.map.FieldKey;
import org.simpleflatmapper.map.MappingContext;
import org.simpleflatmapper.map.MappingException;
import org.simpleflatmapper.map.SourceFieldMapper;
import org.simpleflatmapper.util.ErrorHelper;
import org.simpleflatmapper.util.UnaryFactory;
import org.simpleflatmapper.util.UnaryFactoryWithException;

public class DynamicSourceFieldMapper<ROW, T, K extends FieldKey<K>, E extends Exception> implements SourceFieldMapper<ROW, T> {

    private final MapperCache<K, SourceFieldMapper<ROW, T>> mapperCache;
	private final UnaryFactory<MapperKey<K>, SourceFieldMapper<ROW, T>> mapperFactory;
	private final UnaryFactoryWithException<ROW, MapperKey<K>, E> mapperKeyFromRow;

	public DynamicSourceFieldMapper(
			UnaryFactory<MapperKey<K>,SourceFieldMapper<ROW, T>> mapperFactory,
			UnaryFactoryWithException<ROW, MapperKey<K>, E> mapperKeyFromRow,
			MapperKeyComparator<K> keyComparator) {
		this.mapperFactory = mapperFactory;
		this.mapperKeyFromRow = mapperKeyFromRow;
		this.mapperCache = new MapperCache<K, SourceFieldMapper<ROW, T>>(keyComparator);
	}

	@Override
	public final T map(ROW row) throws MappingException {
		try {
			return getMapperFromRow(row).map(row);
		} catch(Exception e) {
			ErrorHelper.rethrow(e);
			return null;
		}
	}

	@Override
	public final T map(ROW row, MappingContext<? super ROW> context) throws MappingException {
		try {
			return getMapperFromRow(row).map(row, context);
		} catch(Exception e) {
			ErrorHelper.rethrow(e);
			return null;
		}
	}

	@Override
	public void mapTo(ROW row, T target, MappingContext<? super ROW> context) throws Exception {
		try {
			getMapperFromRow(row).mapTo(row, target,context);
		} catch(Exception e) {
			ErrorHelper.rethrow(e);
		}
	}

	@Override
	public String toString() {
		return "DynamicMapper{mapperFactory=" + mapperFactory
				+  ", " + mapperCache +
				'}';
	}

	private SourceFieldMapper<ROW, T> getMapperFromRow(ROW row) throws E {
		return getMapper(mapperKeyFromRow.newInstance(row));
	}

	public SourceFieldMapper<ROW, T> getMapper(MapperKey<K> key) {
		SourceFieldMapper<ROW, T> mapper = mapperCache.get(key);
		if (mapper == null) {
			mapper = mapperFactory.newInstance(key);
			mapperCache.add(key, mapper);
		}
		return mapper;
	}


}
