package com.github.rafaelsouzaf.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.KeyValueItemWriter;

public class Writer<String> extends KeyValueItemWriter<String, String> {

    private static final Logger log = LoggerFactory.getLogger(Writer.class);

    @Override
    protected void writeKeyValue(String s, String s2) {
        log.info("---------------writeKeyValue: " + s + "-----" + s2);
    }

    @Override
    protected void init() {
        log.info("----------------INIT AQUI");
    }
}
