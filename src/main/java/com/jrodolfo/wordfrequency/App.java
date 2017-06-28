package com.jrodolfo.wordfrequency;

/**
 * Main class of the application
 */
public class App
{
    public static void main(String[] args)
    {
        FileParser fileParser = new FileParser();
        for (String fileName : fileParser.getFilesToParse()) {
            fileParser.parse(fileName);
            // fileParser.debug(fileName);
        }
    }
}
