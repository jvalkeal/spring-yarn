package org.springframework.yarn.batch.partition;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component("reader")
public class ExampleItemReader implements ItemReader<String> {

    private String[] input = { "Hello world!", null };

    private int index = 0;

    public String read() throws Exception {
        if (index < input.length) {
            return input[index++];
        } else {
            return null;
        }

    }

}
