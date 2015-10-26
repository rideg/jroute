package org.jroute.collection.buffer;

import org.jroute.collection.UnsafeHelper;
import sun.misc.Unsafe;

public class OffHeapByteBuffer {

    private static final Unsafe UNSAFE = UnsafeHelper.getUnsafe();
    private static final boolean IS_64_BIT = Unsafe.ADDRESS_SIZE == 8;
    private static final long ARRAY_BYTE_BASE_OFFSET = Unsafe.ARRAY_BYTE_BASE_OFFSET;

    private final int capacity;
    private final long address;
    private int offset;

    public OffHeapByteBuffer(final int capacity) {
        address = UNSAFE.allocateMemory(capacity);
        this.capacity = capacity;
    }

    public void read(byte[] source, int offset, int length) {
        UNSAFE.copyMemory(getAddress(source) + offset, this.address + this.offset, length);
        this.offset += offset;
    }

    private long getAddress(final byte[] array) {
        if (IS_64_BIT) {
            return UNSAFE.getLong(array, ARRAY_BYTE_BASE_OFFSET);
        } else {
            return normalize(UNSAFE.getInt(array, ARRAY_BYTE_BASE_OFFSET));
        }
    }

    private long normalize(final int addr) {
        if (addr > 0) {
            return addr;
        }
        return (~0L >>> 32) & addr;
    }

    public int write(byte[] destination, int offset, int length) {
        final int count = length > this.offset ? offset : length;
        final long address = getAddress(destination);
        UNSAFE.copyMemory(this.address, address + offset, count);
        return count;
    }

    public byte get(int index) {
        return UNSAFE.getByte(address + index);
    }

    public void put(int index, byte value) {
        UNSAFE.putByte(address + index, value);
    }

    public int capacity() {
        return capacity;
    }

    public int remaining() {
        return capacity - offset;
    }

    public int clear() {
        return offset = 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        UNSAFE.freeMemory(address);
    }

}
