package org.sfm.tuples;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Tuple3<T1, T2, T3> {

    private final T1 element1;
    private final T2 element2;
    private final T3 element3;

    public Tuple3(T1 element1, T2 element2, T3 element3) {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
    }

    public T1 getElement1() {
        return element1;
    }

    public T2 getElement2() {
        return element2;
    }

    public T3 getElement3() {
        return element3;
    }

    public static <T1, T2, T3> ParameterizedType typeDef(final Class<T1> c1, final Class<T2> c2, final Class<T3> c3) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { c1, c2, c3 };
            }

            @Override
            public Type getRawType() {
                return Tuple2.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
