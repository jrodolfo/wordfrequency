package com.jrodolfo.wordfrequency.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Util class to help with files.
 * <p>
 * Created by Rod Oliveira on 18-Jun-2017
 */
public class Util {

    static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @NotNull
    public static List<String> stringToList(final String listOfElements, final char mark) {
        return Arrays.asList(listOfElements.split("\\s*" + mark + "\\s*"));
    }

    public static void writeToFile(String fileName, String content) {
        if (content != null && !content.isEmpty()) {
            try {
                fileName = "output/" + fileName;
                File file = new File(fileName);
                if (!file.exists()) file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method removes the string "file:/C:" from a file url string
     *
     * @param filePath, a string with protocol and driver, representing a file, for example:
     *                  file:/C:/dev/workspace/word-frequency/target/classes/input/file-03-latin.txt
     * @return the file path without the protocol and driver, for example:
     * /dev/workspace/word-frequency/target/classes/input/file-03-latin.txt
     */
    @NotNull
    public static String removeProtocolAndDriver(String filePath) {
        return filePath.substring(filePath.lastIndexOf(":") + 1);
    }

    /**
     * This method removes the path part of a string representing a file
     *
     * @param filePath, a string with path representing a file, for example:
     *                  input/file-03-latin.txt
     * @return the file path without the protocol and driver, for example:
     * file-03-latin.txt
     */
    @NotNull
    public static String removePath(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * This method removes the path part of a string representing a file
     *
     * @param fileName, a string with path representing a file, for example:
     *                  input/file-03-latin.txt
     * @return the file path without the protocol and driver, for example:
     * file-03-latin.txt
     */
    @NotNull
    public static String removeExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * @param fileName
     * @return if the method finds the file, it returns a string representing the
     * file name and path, without the protocol and driver
     */
    @Nullable
    public static String getFileNameWithPath(String fileName) {
        Class clazz = MethodHandles.lookup().lookupClass();
        ClassLoader classLoader = clazz.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            logger.debug("Resource is null. Check if the file " + fileName + " exists.");
            return null;
        }
        String fileNameWithProtocolDriverAndPath = resource.toExternalForm();
        String fileNameWithPath = removeProtocolAndDriver(fileNameWithProtocolDriverAndPath);
        return fileNameWithPath;
    }
}
