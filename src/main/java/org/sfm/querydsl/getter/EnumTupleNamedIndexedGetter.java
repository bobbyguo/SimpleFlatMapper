package org.sfm.querydsl.getter;

import org.sfm.querydsl.TupleElementKey;
import org.sfm.reflect.Getter;

import com.mysema.query.Tuple;

public final class EnumTupleNamedIndexedGetter<E extends Enum<E>> implements Getter<Tuple, E> {

	private final int index;
	private final Class<E> enumType;
	
	public EnumTupleNamedIndexedGetter(final TupleElementKey key, Class<E> enumType) {
		this.index = key.getIndex();
		this.enumType = enumType;
	}

	@Override
	public E get(final Tuple target) throws Exception {
		final String o = target.get(index, String.class);
		return (E) Enum.valueOf(enumType, String.valueOf(o));
	}
}