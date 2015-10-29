package org.jroute.collection.buffer;

import org.jroute.util.UnsafeHelper;
import sun.misc.Unsafe;

import java.io.IOException;
import java.io.InputStream;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;

public class OffHeapByteBuffer {

    private static final Unsafe UNSAFE = UnsafeHelper.getUnsafe();

    private final int capacity;
    private final long address;
    private int offset;
    private int marker;

    public OffHeapByteBuffer(final int capacity) {
        address = UNSAFE.allocateMemory(capacity);
        this.capacity = capacity;
    }

    public void read(byte[] source, int offset, int length) {
        UNSAFE.copyMemory(source, ARRAY_BYTE_BASE_OFFSET + offset, null, address + this.offset, length);
        this.offset += length;
    }

    public int write(byte[] destination, int offset, int length) {
        final int count = length > this.offset ? this.offset : length;
        UNSAFE.copyMemory(null, this.address, destination, ARRAY_BYTE_BASE_OFFSET + offset, count);
        return count;
    }

    public int readAvailable(final InputStream stream) throws IOException {
        final int available = stream.available();
        int read = 0;
        if (available > 0) {
            checkSpace(available);
            for (; read < available; read++) {
                final byte value = (byte) stream.read();
                UNSAFE.putByte(this.address + offset++, value);
            }
        }
        return read;
    }

    private void checkSpace(final int available) throws IOException {
        if (available > getRemaining()) {
            throw new IOException("Incoming packet is too large");
        }
    }

    public byte get(int index) {
        return UNSAFE.getByte(address + index);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOffset() {
        return offset;
    }

    public int getRemaining() {
        return capacity - offset;
    }

    public int clear() {
        return offset = 0;
    }


    public void mark() {
        marker = offset;
    }

    public void mark(final int position) {
        marker = position;
    }

    public void flip() {
        UNSAFE.copyMemory(address + marker, address, offset - marker);
        offset -= marker;
        marker = 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        UNSAFE.freeMemory(address);
    }

    public static OffHeapByteBuffer wrap(final byte[] array) {
        final OffHeapByteBuffer buffer = new OffHeapByteBuffer(array.length);
        buffer.read(array, 0, array.length);
        return buffer;
    }
}
