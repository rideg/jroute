package org.jroute.collection;

import java.lang.reflect.Constructor;

import sun.misc.Unsafe;

public class CASBlockingQueue<T> {

    private static final Unsafe unsafe;
    private static final long headOffset;
    private static final long tailOffset;

    static {
        try {
            Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            unsafe = unsafeConstructor.newInstance();

            headOffset = unsafe.objectFieldOffset(CASBlockingQueue.class.getField("head"));
            tailOffset = unsafe.objectFieldOffset(CASBlockingQueue.class.getField("tail"));

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static class Node<T> {

        private final T value;
        private Node<T> next;

        public Node(final T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setNext(final Node<T> next) {
            this.next = next;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private volatile Node<T> head;
    private volatile Node<T> tail;

    public void push(final T e) {
        Node<T> node = new Node<>(e);
        boolean updateTail = true;
        Node<T> headCopy = head;

        while (!unsafe.compareAndSwapObject(this, headOffset, headCopy, node)) {
            headCopy = head;
            updateTail = false;
        }
        headCopy.setNext(node);

        if (updateTail && tail == null) {
            unsafe.compareAndSwapObject(this, tailOffset, null, headCopy);
        }
    }

    public T shift() {
        Node<T> tailCopy = getCopy();
        boolean updateHead = true;
        while (!unsafe.compareAndSwapObject(this, tailOffset, tailCopy, tailCopy.getNext())) {
            tailCopy = getCopy();
            updateHead = false;
        }

        if (updateHead && tailCopy.getNext() == null) {
            unsafe.compareAndSwapObject(this, headOffset, tailCopy, null);
        }

        return tailCopy.getValue();
    }

    private Node<T> getCopy() {
        Node<T> tailCopy;
        for (;;) {
            for (int i = 0; i < 100; i++) {
                tailCopy = tail;
                if (tailCopy != null) {
                    return tailCopy;
                }
            }
            Thread.yield();
        }
    }

}
