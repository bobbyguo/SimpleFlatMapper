package org.simpleflatmapper.reflect.meta;

import org.simpleflatmapper.util.BooleanSupplier;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ArrayPropertyFinder<T, E> extends AbstractIndexPropertyFinder<T> {

	private final List<IndexedElement<T, E>> elements = new ArrayList<IndexedElement<T, E>>();


    public ArrayPropertyFinder(ArrayClassMeta<T, E> arrayClassMeta) {
        super(arrayClassMeta);
    }

    @Override
    protected IndexedElement<T, E> getIndexedElement(IndexedColumn indexedColumn) {
        while (elements.size() <= indexedColumn.getIndexValue()) {
            elements.add(new IndexedElement<T, E>(newElementPropertyMeta(elements.size(), "element" + elements.size()), ((ArrayClassMeta<T, E>)classMeta).getElementClassMeta()));
        }

        return elements.get(indexedColumn.getIndexValue());
	}

    private PropertyMeta<T, E> newElementPropertyMeta(int index, String name) {
        ArrayClassMeta<T, E> arrayClassMeta = (ArrayClassMeta<T, E>) classMeta;
        if (arrayClassMeta.isArray()) {
            return new ArrayElementPropertyMeta<T, E>(name,
                    classMeta.getType(), arrayClassMeta.getReflectionService(), index, arrayClassMeta);
        } else {
            return new ListElementPropertyMeta<T, E>(name,
                    classMeta.getType(), arrayClassMeta.getReflectionService(), index, arrayClassMeta, new BooleanSupplier() {

                @Override
                public boolean getAsBoolean() {
                    return elements.size() == 1;
                }
            });
        }
    }

    @Override
    protected void extrapolateIndex(PropertyNameMatcher propertyNameMatcher, FoundProperty foundProperty, PropertyMatchingScore score) {
        final ClassMeta<E> elementClassMeta = ((ArrayClassMeta)classMeta).getElementClassMeta();

        // all element has same type so check if can find property
        PropertyMeta<E, ?> property =
                elementClassMeta.newPropertyFinder().findProperty(propertyNameMatcher);
        if (property != null) {
            for (int i = 0; i < elements.size(); i++) {
                IndexedElement element = elements.get(i);
                if (!element.hasProperty(property)) {
                    lookForAgainstColumn(new IndexedColumn(i, propertyNameMatcher), foundProperty, score);
                    return;
                }
            }

            lookForAgainstColumn(new IndexedColumn(elements.size(), propertyNameMatcher), foundProperty, score);
        }
	}

    @Override
    protected boolean isValidIndex(IndexedColumn indexedColumn) {
        return indexedColumn.getIndexValue() >= 0;
    }

    @Override
    public PropertyFinder<?> getSubPropertyFinder(String name) {
        return null;
    }
}
