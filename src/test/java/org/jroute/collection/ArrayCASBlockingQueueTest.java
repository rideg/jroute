package org.jroute.collection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class ArrayCASBlockingQueueTest {

    @Test
    public void oneElemInOneOut() {
        // given
        ArrayCASBlockingQueue<String> queue = new ArrayCASBlockingQueue<>(10);
        String s = "test string";

        // when
        queue.push(s);
        // and
        String ret = queue.shift();

        // then
        Assert.assertSame(s, ret);

    }

    @Test
    public void twoElemsOrder() {
        // given
        ArrayCASBlockingQueue<String> queue = new ArrayCASBlockingQueue<>(10);
        String s1 = "test string";
        String s2 = "test string2";

        // when
        queue.push(s1);
        queue.push(s2);

        // then
        Assert.assertSame(s1, queue.shift());
        Assert.assertSame(s2, queue.shift());
    }

    public static void main(final String[] args) throws InterruptedException {

        ArrayCASBlockingQueue<Long> q = new ArrayCASBlockingQueue<>(16);
        // CASBlockingQueue<Long> q = new CASBlockingQueue<>();
        // ArrayBlockingQueue<Long> q = new ArrayBlockingQueue<>(10);

        long limit = 10000000l;
        int tc = 4;
        ExecutorService executor = Executors.newFixedThreadPool(tc * 2);
        Runnable producer = () -> {
            long sum = 0;
            long c = 0;
            for (; c < limit; c++) {
                Long m = new Long(c);
                long start = System.nanoTime();
                q.push(m);
                // try {
                // q2.put(m);
                // } catch (Exception e) {
                // }
                long elapsed = System.nanoTime() - start;
                sum += elapsed;
            }
            double avg = sum / limit;

            System.out.println("Push avg: " + avg);
        };

        Runnable consumer = () -> {
            long sum = 0;
            long c = 0;
            for (; c < limit; c++) {
                long start = System.nanoTime();
                q.shift();
                // try {
                // q2.take();
                // } catch (Exception e) {
                // }
                long elapsed = System.nanoTime() - start;
                sum += elapsed;
            }
            double avg = sum / c;

            System.out.println("Shift (avg: " + avg);
        };

        for (int i = 0; i < tc; i++) {
            executor.execute(consumer);
            executor.execute(producer);
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }
}
