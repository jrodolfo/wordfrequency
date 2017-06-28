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
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Rod Oliveira on 08-Sep-2016.
 */
public class FileParser {

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static Properties properties;
    private final static boolean useStopWords;
    private final static String stopWordsFile;
    private final static Set<String> stopWords;
    private List<String> filesToParse;
    private int numberOfWordsPerTerms;
    private Terms terms;

    static {
        properties = PropertyValues.getProperties();
        useStopWords = Boolean.parseBoolean(properties.getProperty("use.stop.words"));
        stopWordsFile = properties.getProperty("stop.words.file");
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
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileNameWithPath))) {
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        write(fileName);
    }

    private static String getFileNameWithPath(String fileName) {
        String fileNameWithPath = Util.getFileNameWithPath(fileName);
        if (fileNameWithPath == null) {
            logger.debug("Could not find the file " + fileName);
            System.exit(-1);
        }
        return fileNameWithPath;
    }

    private void processLine(String line) {
        line = line.trim();
        // regular expression representing any char that is not a blank or letter in any language
        String regExpression = "[^\\p{L} ]";
        if (line.length() > 0) {
            String[] words = line.replaceAll(regExpression, "").toLowerCase().trim().split("\\s+");
            for (String word : words) {
                if (useStopWords && stopWords.contains(word)) continue;
                terms.addWord(word);
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

    public void write(String fileName) {
        fileName = Util.removePath(fileName);
        fileName = Util.removeExtension(fileName);

//        Util.writeToFile(fileWithoutPath + "-ordered-by-word.txt",           terms.getMapOrderedByValueAsc());
//        Util.writeToFile(fileWithoutPath + "-ordered-by-frequency-asc.txt",  terms.getMapOrderedByValueAsc());
        Util.writeToFile(fileName + "-ordered-by-frequency-desc.txt", terms.getMapOrderedByValueDesc());
    }

    public List<String> getFilesToParse() {
        return filesToParse;
    }
}