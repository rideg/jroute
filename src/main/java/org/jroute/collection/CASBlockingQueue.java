package org.jroute.collection;

import sun.misc.Unsafe;

public class CASBlockingQueue<T> {

    private static final Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;

    static {
        UNSAFE = UnsafeHelper.getUnsafe();
        try {
            headOffset = UNSAFE.objectFieldOffset(CASBlockingQueue.class.getField("head"));
            tailOffset = UNSAFE.objectFieldOffset(CASBlockingQueue.class.getField("tail"));
        } catch (NoSuchFieldException e) {
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

        while (!UNSAFE.compareAndSwapObject(this, headOffset, headCopy, node)) {
            headCopy = head;
            updateTail = false;
        }
        headCopy.setNext(node);

        if (updateTail && tail == null) {
            UNSAFE.compareAndSwapObject(this, tailOffset, null, headCopy);
        }
    }

    public T shift() {
        Node<T> tailCopy = getCopy();
        boolean updateHead = true;
        while (!UNSAFE.compareAndSwapObject(this, tailOffset, tailCopy, tailCopy.getNext())) {
            tailCopy = getCopy();
            updateHead = false;
        }

        if (updateHead && tailCopy.getNext() == null) {
            UNSAFE.compareAndSwapObject(this, headOffset, tailCopy, null);
        }

        return tailCopy.getValue();
    }

    private Node<T> getCopy() {
        Node<T> tailCopy;
        for (; ; ) {
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
