package com.jrodolfo.wordfrequency.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

/**
 * This class deals with the properties file by reading it and creating an object Properties
 * Created by Rod Oliveira on 18-Jun-2017
 */
public class PropertyValues {

    private static Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static {
        properties = new Properties();
        InputStream inputStream = null;
        try {
            final String propFileName = "wordfrequency.properties";
            inputStream = MethodHandles.lookup().lookupClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + propFileName + "' was not found in the classpath.");
            }
            logger.debug("files.to.parse=" + properties.getProperty("files.to.parse"));
            logger.debug("use.stop.words=" + properties.getProperty("use.stop.words"));
            logger.debug("stop.words.file=" + properties.getProperty("stop.words.file"));
            logger.debug("number.of.words=" + properties.getProperty("number.of.words.per.term"));
        } catch (Exception e) {
            logger.error("Exception: " + e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Properties getProperties() {
        return properties;
    }
}
