package com.jrodolfo.wordfrequency.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

/**
 * This class deals with the properties file by
 * reading it and creating a Properties object
 *
 * Created by Rod Oliveira (jrodolfo.com) on 2017-06-18
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

            // debug:
            logger.debug("files.to.parse=" + properties.getProperty("files.to.parse"));
            logger.debug("use.stop.words=" + properties.getProperty("use.stop.words"));
            logger.debug("stop.words.file=" + properties.getProperty("stop.words.file"));
            logger.debug("number.of.words=" + properties.getProperty("number.of.words.per.term"));
            logger.debug("minimum.frequency.threshold=" + properties.getProperty("minimum.frequency.threshold"));
            logger.debug("regular.expression=" + properties.getProperty("regular.expression"));

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
