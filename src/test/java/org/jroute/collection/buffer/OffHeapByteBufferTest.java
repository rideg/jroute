package org.jroute.collection.buffer;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OffHeapByteBufferTest {


    @Test
    public void shouldReadFromArray() throws Exception {
        // given
        final OffHeapByteBuffer buffer = new OffHeapByteBuffer(10);
        final byte[] source = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        // when
        buffer.read(source, 0, source.length);

        // then
        for (int i = 0; i < 10; i++) {
            assertThat(buffer.get(i), is((byte) i));
        }

        // and
        assertThat(buffer.getRemaining(), is(0));
    }


    @Test
    public void shouldWriteToArray() throws Exception {
        // given
        final OffHeapByteBuffer buffer = OffHeapByteBuffer.wrap(new byte[]{1, 2, 3});
        final byte[] dest = new byte[5];

        // when
        final int count = buffer.write(dest, 0, 5);

        // then
        assertThat(count, is(3));

        assertThat(dest[0], is((byte) 1));
        assertThat(dest[1], is((byte) 2));
        assertThat(dest[2], is((byte) 3));
        assertThat(dest[3], is((byte) 0));
        assertThat(dest[4], is((byte) 0));
    }

    @Test
    public void shouldReadFromStream() throws Exception {

        // given
        final InputStream stream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final OffHeapByteBuffer buffer = new OffHeapByteBuffer(10);

        // when
        final int read = buffer.readAvailable(stream);

        // then
        assertThat(read, is(5));

        assertThat(buffer.get(0), is((byte) 1));
        assertThat(buffer.get(1), is((byte) 2));
        assertThat(buffer.get(2), is((byte) 3));
        assertThat(buffer.get(3), is((byte) 4));
        assertThat(buffer.get(4), is((byte) 5));

        assertThat(buffer.getRemaining(), is(5));
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionInCaseIfMoreAvailableToReadThanCapacity() throws Exception {
        new OffHeapByteBuffer(3).readAvailable(new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5}));
    }


    @Test
    public void shouldFlipBuffer() throws Exception {
        // given
        final InputStream stream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        final InputStream stream2 = new ByteArrayInputStream(new byte[]{4, 5});
        final OffHeapByteBuffer buffer = new OffHeapByteBuffer(5);

        buffer.readAvailable(stream);
        buffer.mark();
        buffer.readAvailable(stream2);

        // when
        buffer.flip();

        // then
        assertThat(buffer.get(0), is((byte) 4));
        assertThat(buffer.get(1), is((byte) 5));

        assertThat(buffer.getRemaining(), is(3));
    }
}