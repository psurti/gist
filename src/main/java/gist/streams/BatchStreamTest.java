package gist.streams;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.*;

/**
 *  Stream Batch Tests
 */
public final class BatchStreamTest {

    public static final int END_EXCLUSIVE = 30;
    public static final int PARTITION_SIZE = 8;

    private static <T> Collector<T, List<T>, List<List<T>>> partitioned(int chunkSize) {
        return Collector.of(
                ArrayList::new,
                List::add,
                (a, b) -> {
                    a.addAll(b);
                    return a;
                },
                a -> Partition.ofSize(a, chunkSize),
                Collector.Characteristics.UNORDERED);
    }

    private static final long[] free(long start) {
        if (start == 0) {
            start = Runtime.getRuntime().freeMemory();
        }
        System.gc();
        ;
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        long end = Runtime.getRuntime().freeMemory();
        return new long[]{end, (end - start)};
    }

    public static void main(String[] args) {

        TreeMap<String, Long> results = new TreeMap<>();
        long startMem = 0;
        long mem[] = null;
        // partitioning example 1
        System.out.println("Paritioned Stream");
        Stream<String> stream = IntStream.range(0, END_EXCLUSIVE).mapToObj(i -> String.format("%3d", i));
        mem = free(0);
        mem = free(0);
        long start = System.currentTimeMillis();
        Stream<Stream<String>> partitioned1 = partition(stream, PARTITION_SIZE);
        partitioned1.map(s -> s.collect(Collectors.joining(",")))
                .forEachOrdered(System.out::println);
        long stop = System.currentTimeMillis();
        mem = free(mem[0]);
        results.put("Parition Stream -- " + mem[1], (stop - start));
        System.out.println(" -- " + results.lastEntry());

        // exanple of Parition List
        System.out.println("PartitionList");
        List<String> intStream = IntStream.range(0, END_EXCLUSIVE).mapToObj(i -> String.format("%3d", i)).collect(Collectors.toList());
        mem = free(0);
        start = System.currentTimeMillis();
        Partition<String> stringPartition = Partition.ofSize(intStream, PARTITION_SIZE);
        stop = System.currentTimeMillis();
        mem = free(mem[0]);
        stringPartition.forEach(System.out::println);
        results.put("ParitionList -- " + mem[1], (stop - start));
        System.out.println(" -- " + results.lastEntry());

        System.out.println("StreamPartitionList");
        Stream<String> sIntStream = IntStream.range(0, END_EXCLUSIVE).mapToObj(i -> String.format("%3d", i));
        mem = free(0);
        start = System.currentTimeMillis();
        List<List<String>> val = sIntStream.collect(partitioned(PARTITION_SIZE));
        stop = System.currentTimeMillis();
        mem = free(mem[0]);
        val.forEach(System.out::println);
        results.put("StreamPartitionList -- " + mem[1], (stop - start));
        System.out.println(" -- " + results.lastEntry());

        //jdk
        System.out.println("JDK Stream");
        Stream<String> jdkStream = IntStream.range(0, END_EXCLUSIVE).mapToObj(i -> String.format("%3d", i));
        mem = free(0);
        start = System.currentTimeMillis();
        final AtomicInteger counter = new AtomicInteger();
        final Collection<List<String>> result = jdkStream.collect(Collectors.groupingBy(it -> counter.incrementAndGet() / PARTITION_SIZE)).values();
        stop = System.currentTimeMillis();
        mem = free(mem[0]);
        result.forEach(System.out::println);
        results.put("JDKStream -- " + mem[1], (stop - start));
        System.out.println(" -- " + results.lastEntry());

        // partitioning example 2
        stream = IntStream.iterate(0, i -> ++i).mapToObj(i -> String.format("%3d", i));
        Stream<Stream<String>> partitioned2 = partition(stream, PARTITION_SIZE);
        partitioned2.map(s -> s.collect(Collectors.joining(",")))
                .limit(3)
                .forEachOrdered(System.out::println);

        System.out.println(" -- ");

        // heading example
        stream = IntStream.iterate(0, i -> ++i).mapToObj(i -> String.format("%3d", i));
        Stream<String> head = head(stream, PARTITION_SIZE);
        System.out.println(head.collect(Collectors.joining(",")));


        System.out.println(results);
    }

    private static <T> Stream<Stream<T>> partition(Stream<T> stream, int partitionSize) {
        return StreamSupport.stream(new PartitioningSpliterator<>(stream.spliterator(), partitionSize),
                stream.isParallel())
                .map(sp -> StreamSupport.stream(sp, stream.isParallel()));
    }

    private static <T> Stream<T> head(Stream<T> stream, int size) {
        return StreamSupport.stream(new FixedSizeSpliterator<>(stream.spliterator(), size),
                stream.isParallel());
    }

    private static class PartitioningSpliterator<E> extends Spliterators.AbstractSpliterator<Spliterator<E>> {
        private final Spliterator<E> wrapped;
        private final int partitionSize;

        PartitioningSpliterator(Spliterator<E> wrapped, int partitionSize) {
            super(wrapped.estimateSize(), wrapped.characteristics() & ~Spliterator.SORTED);
            this.wrapped = wrapped;
            this.partitionSize = partitionSize;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Spliterator<E>> action) {
            Spliterator<E> s = new FixedSizeSpliterator<>(wrapped, partitionSize);
            action.accept(s);
            return wrapped.estimateSize() != 0;
        }

        @Override
        public long estimateSize() {
            long e = wrapped.estimateSize();
            if (e == Long.MAX_VALUE) {
                return Long.MAX_VALUE;
            }
            long remainder = e % partitionSize;
            if (remainder == 0) {
                return e / partitionSize;
            }
            return e / partitionSize + 1;
        }
    }

    private static class FixedSizeSpliterator<E> extends Spliterators.AbstractSpliterator<E> {
        private final Spliterator<E> wrapped;
        private final long size;
        private final AtomicInteger cursor = new AtomicInteger(0);

        FixedSizeSpliterator(Spliterator<E> wrapped, int limitSize) {
            this(wrapped, size(wrapped, limitSize), wrapped.characteristics());
        }

        private FixedSizeSpliterator(Spliterator<E> wrapped, long size, int characteristics) {
            super(size, characteristics);
            this.wrapped = wrapped;
            this.size = size;
        }

        private static <E> long size(Spliterator<E> wrapped, long size) {
            long e = wrapped.estimateSize();
            if (e > size) {
                return size;
            }
            return e;
        }

        @Override
        public boolean tryAdvance(Consumer<? super E> action) {
            if (cursor.incrementAndGet() > size) {
                return false;
            }
            return wrapped.tryAdvance(action);
        }

        @Override
        public long estimateSize() {
            long s = size(wrapped, size - cursor.get());
            return s > 0 ? s : 0;
        }

        @Override
        public Comparator<? super E> getComparator() {
            if (wrapped.hasCharacteristics(Spliterator.SORTED)) {
                return wrapped.getComparator();
            }
            return super.getComparator();
        }
    }
}

