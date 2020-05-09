package loop;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

@BenchmarkMode(Mode.Throughput)
public class BenchmarkTest {

    @Benchmark
    public void testLoop() {
        for (int i = 0; i < 1_000_000; i++) {
            i = i * 12;
        }
    }
}
