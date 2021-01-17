package ch.edulearn;

import ch.edulearn.entities.SearchResult;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * A simple script that uses jmh to benchmark List creations and iterations
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class ListsBenchmarks {

    // Perform the benchmarks with 10'000 elements
    @Param({"10000"})
    public int N;

    private ArrayList<SearchResult> ARRAY_LIST_ITERATION_TEST;

    private LinkedList<SearchResult> LINKED_LIST_ITERATION_TEST;

    public static void main(String[] args) throws RunnerException {

        // Initialise the jmk benchmark
        Options opt = new OptionsBuilder()
                .include(ListsBenchmarks.class.getSimpleName())
                .forks(1)
                .build();

        // Run JMH benchmarks
        new Runner(opt).run();

    }

    @Setup
    public void setup() {

        ARRAY_LIST_ITERATION_TEST = createArrayList();
        LINKED_LIST_ITERATION_TEST = createLinkedList();
    }

    /**
     * Test ArrayList creation and fill with SearchResults
     * ArrayList is created directly with the right size
     */
    @Benchmark
    public void benchArrayListCreationWithSearchResults(Blackhole b) {

        var list = new ArrayList<SearchResult>(N);
        for (int i = 0; i < N; i++) {
            list.add(new SearchResult(
                    "Test" + N,
                    "toto",
                    "tutu",
                    "tata",
                    1
            ));
        }
        b.consume(list);
    }

    /**
     * Test ArrayList creation and fill with SearchResults
     * ArrayList is created with his defalault size
     */
    @Benchmark
    public void benchDefaultSizeArrayListCreationWithSearchResults(Blackhole b) {

        var list = new ArrayList<SearchResult>();
        for (int i = 0; i < N; i++) {
            list.add(new SearchResult(
                    "Test" + N,
                    "toto",
                    "tutu",
                    "tata",
                    1
            ));
        }
        b.consume(list);
    }

    /**
     * Test LinkedList creation and fill with SearchResults
     */
    @Benchmark
    public void benchLinkedListCreationWithSearchResults(Blackhole b) {

        var list = new LinkedList<SearchResult>();
        for (int i = 0; i < N; i++) {
            list.add(new SearchResult(
                    "Test" + N,
                    "toto",
                    "tutu",
                    "tata",
                    1
            ));
        }
        b.consume(list);
    }

    /**
     * ArrayList iteration with a foreach loop
     */
    @Benchmark
    public void benchIterationOnArrayListWithForEach(Blackhole b) {

        for (SearchResult s : ARRAY_LIST_ITERATION_TEST) {
            b.consume(s);
        }
    }

    /**
     * ArrayList iteration with a for loop
     */
    @Benchmark
    public void benchIterationOnArrayListWithFor(Blackhole b) {

        for (int i = 0; i < ARRAY_LIST_ITERATION_TEST.size(); i++) {
            var s = ARRAY_LIST_ITERATION_TEST.get(i);
            b.consume(s);
        }
    }

    /**
     * LinkedList iteration with a foreach loop
     */
    @Benchmark
    public void benchIterationOnLinkedListWithForEach(Blackhole b) {

        for (SearchResult s : LINKED_LIST_ITERATION_TEST) {
            b.consume(s);
        }
    }

    /**
     * LinkedList iteration with a for loop
     */
    @Benchmark
    public void benchIterationOnLinkedListWithFor(Blackhole b) {

        for (int i = 0; i < LINKED_LIST_ITERATION_TEST.size(); i++) {
            var s = LINKED_LIST_ITERATION_TEST.get(i);
            b.consume(s);
        }
    }

    /**
     * Create an LinkedList with N elements for future iteration tests
     */
    private LinkedList<SearchResult> createLinkedList() {

        var list = new LinkedList<SearchResult>();
        for (int i = 0; i < N; i++) {
            list.add(new SearchResult(
                    "Test" + N,
                    "toto",
                    "tutu",
                    "tata",
                    1
            ));
        }
        return list;
    }

    /**
     * Create an ArrayList with N elements for future iteration tests
     */
    private ArrayList<SearchResult> createArrayList() {

        var list = new ArrayList<SearchResult>();
        for (int i = 0; i < N; i++) {
            list.add(new SearchResult(
                    "Test" + N,
                    "toto",
                    "tutu",
                    "tata",
                    1
            ));
        }
        return list;
    }

}
