package com.jrodolfo.wordfrequency;

import java.util.*;

import static com.jrodolfo.wordfrequency.Terms.SortOrder.ASC;
import static com.jrodolfo.wordfrequency.Terms.SortOrder.DESC;


/**
 * This class holds all terms found inside the file being parsed,
 * with its frequency.
 *
 * Created by Rod Oliveira (jrodolfo.com) on 2017-06-18
 */
public class Terms {

    public enum SortOrder {
        ASC, DESC
    }

    Map<String, Integer> terms = new TreeMap<>();

    public void addTerm(String word) {
        if (terms.containsKey(word)) {
            Integer count = terms.get(word);
            count++;
            terms.put(word, count);
        } else {
            terms.put(word, 1);
        }
    }

    public String getMapOrderedByKey() {
        return toString(terms);
    }

    public String getMapOrderedByValueAsc() {
        Map<String, Integer> sortedMapByValueAsc = sortMapByValue(terms, ASC);
        return toString(sortedMapByValueAsc);
    }

    public String getMapOrderedByValueDesc() {
        Map<String, Integer> sortedMapByValueDesc = sortMapByValue(terms, DESC);
        return toString(sortedMapByValueDesc);
    }

    public static String toString(Map<String, Integer> map) {
        if (map == null) return "";
        StringBuilder result = new StringBuilder();
        for (String term : map.keySet()) {
            result.append(term + "," + map.get(term) + "\n");
        }
        return result.toString();
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
