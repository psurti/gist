package gist.loop;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

@BenchmarkMode(Mode.Throughput)
public class BenchmarkTest {

    @Benchmark
    @Test
    @Fork(3)
    public void testLoop() {
        for (int i = 0; i < 1_000_000; i++) {
            i = i * 12;
        }
    }
}
