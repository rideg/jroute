package org.jroute.collection;

import static java.lang.Integer.numberOfLeadingZeros;
import static org.jroute.util.JRouteUtil.getOffset;
import static org.jroute.util.JRouteUtil.getUnsafe;
import sun.misc.Unsafe;

@SuppressWarnings("unchecked")
public class ArrayCASBlockingQueue<T> {

    private static final long HEAD_OFFSET = getOffset(ArrayCASBlockingQueue.class, "head");
    private static final long TAIL_OFFSET = getOffset(ArrayCASBlockingQueue.class, "tail");
    private static final long GUARD_OFFSET = getOffset(ArrayCASBlockingQueue.class, "tailGuard");
    private static final int BASE = Unsafe.ARRAY_OBJECT_BASE_OFFSET;
    private static final int SHIFT = 31 - numberOfLeadingZeros(Unsafe.ARRAY_OBJECT_INDEX_SCALE);
    private static final int LIMIT = 1024;

    private final Object[] array;
    private volatile int head;
    private volatile int tail;
    private volatile int tailGuard;

    public ArrayCASBlockingQueue(final int size) {
        array = new Object[calculate(size)];
    }

    private static int calculate(final int capacity) {
        int i = 1;
        while ((i <<= 1) <= capacity) {
        }
        return i;
    }

    public void push(final T value) {
        int i = 0;
        for (;;) {
            final int _head = head;
            final int _tail = tail;
            if (notFull(_head, _tail) && _head == tailGuard) {
                if (getUnsafe().compareAndSwapInt(this, HEAD_OFFSET, _head, _head + 1)) {
                    getUnsafe().putOrderedObject(array, offset(_head), value);
                    getUnsafe().putOrderedInt(this, GUARD_OFFSET, _head + 1);
                    return;
                }
            }
            i = incWait(i);
        }
    }

    public T shift() {
        int i = 0;
        for (;;) {
            final int _tail = tail;
            if (notEmpty(_tail)) {
                final T elem = getElem(_tail);
                if (getUnsafe().compareAndSwapInt(this, TAIL_OFFSET, _tail, _tail + 1)) {
                    return elem;
                }
            }
            i = incWait(i);
        }
    }

    private boolean notEmpty(final int _tail) {
        return tailGuard > _tail;
    }

    private boolean notFull(final int head, final int tail) {
        return tail + array.length > head;
    }

    private int incWait(final int i) {
        if (i == LIMIT) {
            Thread.yield();
            return 0;
        }
        return i + 1;
    }

    private T getElem(final int index) {
        return (T) getUnsafe().getObjectVolatile(array, offset(index));
    }

    private long offset(final int i) {
        return ((long) (i & array.length - 1) << SHIFT) + BASE;
    }

}
