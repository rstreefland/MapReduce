package uk.co.streefland.rhys.mapreduce.framework;

import java.util.*;

/**
 * Generic shuffler for all map and reduce operations
 *
 * @author Rhys Streefland
 * @version 1.0
 */
public final class Shuffler {

    /**
     * Executes the shuffler
     *
     * @param input List of mapped (Key, Value) pairs
     * @return List of shuffled (Key, List(Value)) pairs
     */
    public List<Pair<String, List>> execute(List<Pair> input) {

        List<Pair<String, List>> output;
        output = shuffle(input);
        return output;
    }

    /**
     * Shuffles and sorts the list of (Key, Value) pairs
     *
     * @param input Single (Key, Value) pair
     * @return List of shuffled (Key, List(Value)) pairs
     */
    private List<Pair<String, List>> shuffle(List<Pair> input) {

        List<Pair<String, List>> output = new ArrayList<>();
        HashMap<String, List<Object>> hashMap = new HashMap<>();

        // For each (key, value) pair in the list
        for (Pair p : input) {
            String key = (String) p.getKey();
            Object value = p.getValue();

            // Remove duplicate keys by adding each (key, value) pair to a HashMap
            if (!hashMap.containsKey(key)) {
                List<Object> values = new ArrayList<>();
                values.add(value);
                hashMap.put(key, values);
            } else {
                List<Object> values = hashMap.get(key);
                values.add(value);
                hashMap.put(key, values);
            }
        }

        // Add the contents of the HashMap to the output list
        for (Map.Entry entry : hashMap.entrySet()) {
            String key = (String) entry.getKey();
            List values = (List) entry.getValue();

            output.add(new Pair<>(key, values));
        }

        // Sort the list by key
        output.sort(Comparator.comparing(Pair::getKey));

        return output;
    }
}
