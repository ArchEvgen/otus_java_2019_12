package ru.otus.hw03;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class DiyArrayList<T> implements List<T> {
    private T[] array;
    private int size = 0;
    private transient int version = 0;
    private final static int MIN_CAPACITY = 8;

    public DiyArrayList() {
        this(MIN_CAPACITY);
    }

    public DiyArrayList(int capacity) {
        array = (T[]) new Object[capacity];
    }

    public DiyArrayList(Collection<? extends T> c) {
        size = c.size();
        array = (T[]) c.toArray();
        if (size < MIN_CAPACITY) {
            growTo(MIN_CAPACITY);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return new DiyListIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        version ++;
        if (array.length == size) {
            grow();
        }
        array[size] = t;
        size ++;
        return true;
    }

    @Override
    public void add(int index, T element) {
        if (index > size) {
            throw new IndexOutOfBoundsException();
        }
        version ++;
        if (array.length == size) {
            grow();
        }
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = element;
        size ++;
    }

    @Override
    public boolean addAll(Collection<? extends T> source) {
        version ++;
        var newSize = source.size() + size;
        if (newSize > array.length) {
            growTo(newSize);
        }
        for (var c : source) {
            array[size] = c;
            size ++;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        version ++;
        size = 0;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public T set(int index, T element) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return array[index] = element;
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DiyListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DiyListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "[" + this.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
    }

    private void grow() {
        var newCapacity = array.length << 1;
        growTo(newCapacity);
    }

    private void growTo(int newCapacity) {
        var newArray = (T[]) new Object[newCapacity];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    private class DiyListIterator implements ListIterator<T> {
        private transient int iteratorVersion = version;
        private transient int cursor;
        private transient int lastIndex = -1;

        public DiyListIterator() {
            this(0);
        }

        public DiyListIterator(int index) {
            cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public T next() {
            checkVersion();
            return DiyArrayList.this.get(lastIndex = cursor++);
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public T previous() {
            checkVersion();
            return DiyArrayList.this.get(lastIndex = --cursor);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            checkVersion();
            DiyArrayList.this.set(lastIndex, t);
            iteratorVersion = version;
        }

        @Override
        public void add(T t) {
            checkVersion();
            DiyArrayList.this.add(cursor ++, t);
            iteratorVersion = version;
        }

        private void checkVersion() {
            if (version != iteratorVersion)
                throw new ConcurrentModificationException();
        }
    }
}
