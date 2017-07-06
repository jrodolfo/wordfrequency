package com.jrodolfo.wordfrequency;

import java.util.*;

import static com.jrodolfo.wordfrequency.Terms.SortOrder.ASC;
import static com.jrodolfo.wordfrequency.Terms.SortOrder.DESC;


/**
 * This class holds all map found inside the file being parsed,
 * with its frequency.
 *
 * Created by Rod Oliveira (jrodolfo.com) on 2017-06-18
 */
public class Terms {

    public enum SortOrder {
        ASC, DESC
    }

    Map<String, Integer> map;

    public Terms() {
        map = new TreeMap<>();
    }

    public void clearMap() {
        map.clear();
    }

    public int getNumberOfTerms() {
        return map.size();
    }

    public void addTerm(String word) {
        if (map.containsKey(word)) {
            Integer count = map.get(word);
            count++;
            map.put(word, count);
        } else {
            map.put(word, 1);
        }
    }

    public void removeTermsWithFrequencyLowerThan(int frequency) {
        Map<String, Integer> mapAux = new TreeMap<>();
        String key;
        Integer value;
        for (Map.Entry<String, Integer> node : map.entrySet()) {
            key = node.getKey();
            value = node.getValue();
            if (value >= frequency) {
                mapAux.put(key, value);
            }
        }
        map = mapAux;
    }

    public String getMapOrderedByKey() {
        return toString(map);
    }

    public String getMapOrderedByValueAsc() {
        Map<String, Integer> sortedMapByValueAsc = sortMapByValue(map, ASC);
        return toString(sortedMapByValueAsc);
    }

    public String getMapOrderedByValueDesc() {
        Map<String, Integer> sortedMapByValueDesc = sortMapByValue(map, DESC);
        return toString(sortedMapByValueDesc);
    }

    public static String toString(Map<String, Integer> map) {
        if (map == null) return "";
        StringBuilder result = new StringBuilder();
        for (String term : map.keySet()) {
            result.append(term + "," + map.get(term) + "\n");
        }
        return result.toString().trim();
    }

    private static Map<String, Integer> sortMapByValue(Map<String, Integer> map, final SortOrder order) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> objectOne,
                               Map.Entry<String, Integer> objectTwo) {
                if (order.equals(ASC)) {
                    return objectOne.getValue().compareTo(objectTwo.getValue());
                } else {
                    return objectTwo.getValue().compareTo(objectOne.getValue());
                }
            }
        });
        Map<String, Integer> mapSortedByValue = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            mapSortedByValue.put(entry.getKey(), entry.getValue());
        }
        return mapSortedByValue;
    }

}
