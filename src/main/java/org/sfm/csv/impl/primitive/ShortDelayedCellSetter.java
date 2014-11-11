package org.sfm.csv.impl.primitive;

import org.sfm.csv.impl.DelayedCellSetter;
import org.sfm.csv.impl.ParsingContext;
import org.sfm.csv.impl.cellreader.IntegerCellValueReader;
import org.sfm.reflect.primitive.ShortSetter;

public class ShortDelayedCellSetter<T> implements DelayedCellSetter<T, Short> {

	private final ShortSetter<T> setter;
	private short value;
	
	public ShortDelayedCellSetter(ShortSetter<T> setter) {
		this.setter = setter;
	}

	@Override
	public Short getValue() {
		return new Short(getShort());
	}

	public short getShort() {
		short v = value;
		value = 0;
		return v;
	}
	
	@Override
	public void set(T t) throws Exception {
		short v = value;
		value = 0;
		setter.setShort(t, v);
	}

	@Override
	public boolean isSettable() {
		return setter != null;
	}

	@Override
	public void set(char[] chars, int offset, int length, ParsingContext parsingContext) throws Exception {
		this.value = (short)IntegerCellValueReader.parseInt(chars, offset, length);
	}
}