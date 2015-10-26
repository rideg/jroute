package org.jroute.collection.buffer;

import org.junit.Test;

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
        assertThat(buffer.remaining(), is(0));
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
}