package com.github.rafaelsouzaf.app;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * Created by rsouza on 14-03-18.
 */
public class CustomItemReader<String> implements ItemReader<String> {

    String item;

    public CustomItemReader(String item) {
        this.item = item;
    }

    public String read() throws Exception, UnexpectedInputException,
            NonTransientResourceException, ParseException {

        return this.item;
    }
}