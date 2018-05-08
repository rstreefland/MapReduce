package uk.co.streefland.rhys.mapreduce.framework;

import uk.co.streefland.rhys.mapreduce.Driver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Manages the execution of all mappers. Uses a FixedThreadPool to execute mappers in parallel.
 * Mappers must extend this class and define the map method.
 * @author Rhys Streefland
 * @version 1.0
 */
public abstract class Mapper {

    public final ExecutorService executorService = Executors.newFixedThreadPool(Driver.MAPPER_THREADS);
    public final List<Future> futures = new ArrayList<>();
    public final List<Pair> output = new ArrayList<>();

    /**
     * Controls the execution of each mapper
     * @param input String array representing each line of an input file
     * @return List of (Key, Value) pairs
     */
    public List<Pair> execute(String[] input) {

        for (String line : input) {
            Future future = executorService.submit(() -> map(line));
            futures.add(future);
        }

        collectResults(output);
        return output;
    }

    /**
     * Collects the results of all jobs submitted to the executorService
     * @param output List of (Key, Value) pairs
     */
    public void collectResults(List<Pair> output) {

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

    public abstract Pair map(String input);
}
