package com.jrodolfo.wordfrequency;

import com.jrodolfo.wordfrequency.util.PropertyValues;
import com.jrodolfo.wordfrequency.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * Main class of this application, responsible for parsing
 * the data, and saving the results in output files.
 *
 * Created by Rod Oliveira (jrodolfo.com) on 2017-06-18
 */
public class FileParser {

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static Properties properties;
    private final static boolean useStopWords;
    private final static String stopWordsFile;
    private final static Set<String> stopWords;
    private final static int numberOfWordsPerTerm;
    private final static int minimumFrequencyThreshold;


    private List<String> filesToParse;
    private int numberOfWordsPerTerms;
    private Terms terms;
    private List<String> listOfWords;

    static {
        properties = PropertyValues.getProperties();
        useStopWords = Boolean.parseBoolean(properties.getProperty("use.stop.words"));
        stopWordsFile = properties.getProperty("stop.words.file");
        numberOfWordsPerTerm = Integer.parseInt(properties.getProperty("number.of.words.per.term"));
        minimumFrequencyThreshold = Integer.parseInt(properties.getProperty("minimum.frequency.threshold"));
        if (useStopWords) {
            stopWords = getStopWords(stopWordsFile);
        } else {
            stopWords = null;
        }
    }

    public FileParser() {
        filesToParse = Util.stringToList(properties.getProperty("files.to.parse"), ',');
        numberOfWordsPerTerms = Integer.parseInt(properties.getProperty("number.of.words.per.term"));
    }

    private static Set getStopWords(String fileName) {
        String fileNameWithPath = getFileNameWithPath(fileName);
        Set<String> setOfStopWords = new TreeSet();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileNameWithPath))) {
            while ((line = br.readLine()) != null) {
                setOfStopWords.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return setOfStopWords;
    }

    public void parse(String fileName) {
        String fileNameWithPath = getFileNameWithPath(fileName);
        terms = new Terms();
        listOfWords = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileNameWithPath))) {
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
            processListOfWords(numberOfWordsPerTerms);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        write(fileName);
    }

    private void processListOfWords(int numberOfWords) {
        if (numberOfWords > 1) {
            int start = 0;
            int end = numberOfWords - 1;
            int sizeOfList = listOfWords.size();
            List<String> subList;
            StringBuilder term;
            String sep = " ";
            while (end < sizeOfList) {
                term = new StringBuilder();
                subList = listOfWords.subList(start, end + 1);
                for (String str : subList) {
                    term.append(sep).append(str);
                }
                terms.addTerm(term.toString().trim());
                start++;
                end++;
            }
        } else {
            for (String word : listOfWords) {
                terms.addTerm(word);
            }
        }
    }

    /**
     * This method process a non-null string representing a line in file being parsed. It removes
     * all characters that are not blank, letters (ascii or foreign language), digits,
     * underscore, and minus sign. If the class is using stop words, it removes them
     * @param line
     */
    private void processLine(String line) {
        if (line == null) throw new IllegalArgumentException("this method does not accept null line");
        line = line.trim();
        // Regular expression representing any char that is not:
        //     1) a blank
        //     2) a letter in any language
        //     3) digits
        //     4) underscore
        //     5) minus sign
        String regExpression = "[^\\p{L}0-9_\\- ]";
        if (line.length() > 0) {
            String[] words = line.replaceAll(regExpression, "").toLowerCase().trim().split("\\s+");
            for (String word : words) {
                if (useStopWords && stopWords.contains(word)) continue;
                listOfWords.add(word);
            }
        }
    }

    public void debug(String fileName) {
        logger.debug("\n\n\tWord frequency for file " + fileName + " ordered by words in ascending order:\n");
        logger.debug("\n\n" + terms.getMapOrderedByKey());
        logger.debug("\n\n\tWord frequency for file " + fileName + " ordered by frequency in ascending order:\n");
        logger.debug("\n\n" + terms.getMapOrderedByValueAsc() + "\n");
        logger.debug("\n\n\tWord frequency for file " + fileName + " ordered by frequency in descending order:\n");
        logger.debug("\n\n" + terms.getMapOrderedByValueDesc() + "\n");
        if (stopWords != null) {
            logger.debug("\n\n\tStop words from file " + stopWordsFile + ":\n");
            logger.debug("\n\n" + stopWords.toString() + "\n");
        }
    }

    private static String getFileNameWithPath(String fileName) {
        String fileNameWithPath = Util.getFileNameWithPath(fileName);
        if (fileNameWithPath == null) {
            logger.debug("Could not find the file " + fileName);
            System.exit(-1);
        }
        return fileNameWithPath;
    }

    public void write(String fileName) {
        fileName = Util.removePath(fileName);
        fileName = Util.removeExtension(fileName);
        if (minimumFrequencyThreshold > 1) {
            terms.removeTermsWithFrequencyLowerThan(minimumFrequencyThreshold);
        }
        // Util.writeToFile(fileName + "-ordered-by-word.txt",           terms.getMapOrderedByValueAsc());
        // Util.writeToFile(fileName + "-ordered-by-frequency-asc.txt",  terms.getMapOrderedByValueAsc());
        Util.writeToFile(fileName + "-ordered-by-frequency-desc.txt", terms.getMapOrderedByValueDesc());
    }

    public List<String> getFilesToParse() {
        return filesToParse;
    }
}