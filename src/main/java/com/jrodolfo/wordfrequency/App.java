package com.jrodolfo.wordfrequency;

/**
 * Start point of this application.
 *
 * Created by Rod Oliveira (jrodolfo.com) on 2017-06-18
 */
public class App
{
    public static void main(String[] args)
    {
        FileParser fileParser = new FileParser();
        for (String fileName : fileParser.getFilesToParse()) {
            fileParser.parse(fileName);
        }
    }
}
