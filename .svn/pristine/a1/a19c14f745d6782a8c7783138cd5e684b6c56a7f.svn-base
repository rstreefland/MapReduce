package uk.co.streefland.rhys.mapreduce.framework;

import uk.co.streefland.rhys.mapreduce.Driver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Manages the execution of all reducers. Uses a FixedThreadPool to execute reducers in parallel.
 * Reducers must extend this class and define the reduce method.
 * @author Rhys Streefland
 * @version 1.0
 */
public abstract class Reducer {

    private final ExecutorService executorService = Executors.newFixedThreadPool(Driver.REDUCER_THREADS);
    private final List<Future> futures = new ArrayList<>();
    private final List<Pair> output = new ArrayList<>();

    /**
     * Controls the execution of each reducer
     * @param input List of shuffled (Key, List(Value)) pairs
     * @return List of (Key, Value) pairs
     */
    public List<Pair> execute(List<Pair<String, List>> input) {

        for (Pair<String, List> pair : input) {
            Future future = executorService.submit(() -> reduce(pair, input));
            futures.add(future);
        }

        collectResults(output);
        return output;
    }

    /**
     * Collects the results of all jobs submitted to the executorService
     * @param output List of (Key, Value) pairs
     */
    private void collectResults(List<Pair> output) {

        for (Future future : futures) {
            try {
                Pair outputPair = (Pair) future.get();

                if (outputPair != null) {
                    output.add(outputPair);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }

    /**
     * Abstract method to be extended by every mapper.
     * @param input A single shuffled (Key, List(Value)) pair
     * @param shuffled List of shuffled (Key, List(Value)) pairs
     * @return List of (Key, Value) pairs
     */
    public abstract Pair reduce(Pair<String, List> input, List<Pair<String, List>> shuffled);
}
